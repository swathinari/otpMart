package com.otpmart.cart.dto;

import com.otpmart.cart.entity.CartItem;

import java.time.LocalDateTime;
import java.util.List;

public class CartResponse {

    private String userId;
    private List<CartItem> items;
    private double totalPrice;
    private LocalDateTime updatedAt;

    public CartResponse() {
    }

    public CartResponse(String userId, List<CartItem> items, double totalPrice, LocalDateTime updatedAt) {
        this.userId = userId;
        this.items = items;
        this.totalPrice = totalPrice;
        this.updatedAt = updatedAt;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}