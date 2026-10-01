package com.otpmart.cart.repository;

import com.otpmart.cart.entity.Cart;
import org.springframework.stereotype.Repository;

@Repository
public class CartRepository {

    private final CartJpaRepository cartJpaRepository;

    public CartRepository(CartJpaRepository cartJpaRepository) {
        this.cartJpaRepository = cartJpaRepository;
    }

    public Cart findByUserId(String userId) {
        return cartJpaRepository.findById(userId).orElse(null);
    }

    public void save(Cart cart) {
        cartJpaRepository.save(cart);
    }

    public void deleteByUserId(String userId) {
        cartJpaRepository.deleteById(userId);
    }
}