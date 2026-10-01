package com.otpmart.cart.service;

import com.otpmart.cart.client.ProductServiceClient;
import com.otpmart.cart.dto.AddItemRequest;
import com.otpmart.cart.dto.ProductDto;
import com.otpmart.cart.entity.Cart;
import com.otpmart.cart.entity.CartItem;
import com.otpmart.cart.repository.CartRepository;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final ProductServiceClient productServiceClient;
    private final RedisTemplate<String, Object> redisTemplate;

    public CartService(
            CartRepository cartRepository,
            ProductServiceClient productServiceClient,
            RedisTemplate<String, Object> redisTemplate) {

        this.cartRepository = cartRepository;
        this.productServiceClient = productServiceClient;
        this.redisTemplate = redisTemplate;
    }

    private String cacheKey(String userId) {
        return "cart:" + userId;
    }

    // Get cart from Redis or PostgreSQL
    // Get cart from Redis or PostgreSQL
    public Cart getCart(String userId) {

        String key = cacheKey(userId);

        Object cached = redisTemplate.opsForValue().get(key);

        if (cached instanceof Cart) {
            return (Cart) cached;
        }

        Cart cart = cartRepository.findByUserId(userId);

        if (cart == null) {
            cart = new Cart(userId);
        } else {
            // Convert Hibernate PersistentBag to normal ArrayList
            cart.setItems(new ArrayList<>(cart.getItems()));
        }

        redisTemplate.opsForValue().set(key, cart);

        return cart;
    }

    // Add item to cart
    public Cart addItem(String userId, AddItemRequest request) {

        Cart cart = getCart(userId);

        cart.getItems().stream()
                .filter(item ->
                        item.getProductId().equals(request.getProductId()))
                .findFirst()
                .ifPresentOrElse(
                        existing -> existing.setQuantity(
                                existing.getQuantity() + request.getQuantity()),

                        () -> {
                            ProductDto product =
                                    productServiceClient.getProductById(
                                            request.getProductId());

                            CartItem newItem = new CartItem(
                                    request.getProductId(),
                                    product.getName(),
                                    product.getPrice(),
                                    request.getQuantity()
                            );

                            cart.getItems().add(newItem);
                        }
                );

        cart.setUpdatedAt(LocalDateTime.now());

        cartRepository.save(cart);

        evictCache(userId);

        return cart;
    }

    // Update item quantity
    public Cart updateItemQuantity(
            String userId, String productId, Integer quantity) {

        Cart cart = getCart(userId);

        cart.getItems().stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst()
                .ifPresent(item -> item.setQuantity(quantity));

        cart.setUpdatedAt(LocalDateTime.now());

        cartRepository.save(cart);

        evictCache(userId);

        return cart;
    }

    // Remove item
    public Cart removeItem(String userId, String productId) {

        Cart cart = getCart(userId);

        cart.getItems().removeIf(
                item -> item.getProductId().equals(productId));

        cart.setUpdatedAt(LocalDateTime.now());

        cartRepository.save(cart);

        evictCache(userId);

        return cart;
    }

    // Clear cart
    public void clearCart(String userId) {

        cartRepository.deleteByUserId(userId);

        evictCache(userId);
    }

    // Remove cached cart
    private void evictCache(String userId) {
        redisTemplate.delete(cacheKey(userId));
    }
}