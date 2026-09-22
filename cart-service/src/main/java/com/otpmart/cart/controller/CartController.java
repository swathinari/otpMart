package com.otpmart.cart.controller;

import com.otpmart.cart.dto.AddItemRequest;
import com.otpmart.cart.dto.CartResponse;
import com.otpmart.cart.entity.Cart;
import com.otpmart.cart.service.CartService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Cart> getCart(@PathVariable String userId) {
        return ResponseEntity.ok(cartService.getCart(userId));
    }

    @PostMapping("/{userId}/items")
    public ResponseEntity<CartResponse> addItem(
            @PathVariable String userId,
            @RequestBody @Valid AddItemRequest request) {

        Cart updatedCart = cartService.addItem(userId, request);
        CartResponse response = new CartResponse(
                updatedCart.getUserId(),
                updatedCart.getItems(),
                updatedCart.getTotalPrice(),
                updatedCart.getUpdatedAt()
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{userId}/items/{productId}")
    public ResponseEntity<Cart> updateItem(
            @PathVariable String userId,
            @PathVariable String productId,
            @RequestParam Integer quantity) {

        Cart updatedCart = cartService.updateItemQuantity(userId, productId, quantity);
        return ResponseEntity.ok(updatedCart);
    }

    @DeleteMapping("/{userId}/items/{productId}")
    public ResponseEntity<Cart> removeItem(
            @PathVariable String userId,
            @PathVariable String productId) {

        Cart updatedCart = cartService.removeItem(userId, productId);
        return ResponseEntity.ok(updatedCart);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> clearCart(@PathVariable String userId) {
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }
}