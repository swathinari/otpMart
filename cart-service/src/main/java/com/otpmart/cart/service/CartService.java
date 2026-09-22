package com.otpmart.cart.service;

import com.otpmart.cart.client.ProductServiceClient;
import com.otpmart.cart.dto.AddItemRequest;
import com.otpmart.cart.dto.ProductDto;
import com.otpmart.cart.entity.Cart;
import com.otpmart.cart.entity.CartItem;
import com.otpmart.cart.repository.CartRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final ProductServiceClient productServiceClient;

    public CartService(CartRepository cartRepository, ProductServiceClient productServiceClient) {
        this.cartRepository = cartRepository;
        this.productServiceClient = productServiceClient;
    }

    public Cart getCart(String userId) {
        Cart cart = cartRepository.findByUserId(userId);
        return cart != null ? cart : new Cart(userId);
    }

    public Cart addItem(String userId, AddItemRequest request) {
        Cart cart = getCart(userId);

        cart.getItems().stream()
                .filter(item -> item.getProductId().equals(request.getProductId()))
                .findFirst()
                .ifPresentOrElse(
                        existing -> existing.setQuantity(existing.getQuantity() + request.getQuantity()),
                        () -> {
                            ProductDto product = productServiceClient.getProductById(request.getProductId());

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
        return cart;
    }

    public Cart updateItemQuantity(String userId, String productId, Integer quantity) {
        Cart cart = getCart(userId);

        cart.getItems().stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst()
                .ifPresent(item -> item.setQuantity(quantity));

        cart.setUpdatedAt(LocalDateTime.now());
        cartRepository.save(cart);
        return cart;
    }

    public Cart removeItem(String userId, String productId) {
        Cart cart = getCart(userId);
        cart.getItems().removeIf(item -> item.getProductId().equals(productId));
        cart.setUpdatedAt(LocalDateTime.now());
        cartRepository.save(cart);
        return cart;
    }

    public void clearCart(String userId) {
        cartRepository.deleteByUserId(userId);
    }
}