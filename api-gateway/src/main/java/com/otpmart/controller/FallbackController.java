package com.otpmart.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @GetMapping("/product")
    public ResponseEntity<Map<String, Object>> productFallback() {

        return ResponseEntity
                .status(503)
                .body(Map.of(
                        "status", 503,
                        "error", "Service Unavailable",
                        "message", "Product service is temporarily unavailable"
                ));
    }

    @GetMapping("/user")
    public ResponseEntity<Map<String, Object>> userFallback() {

        return ResponseEntity
                .status(503)
                .body(Map.of(
                        "status", 503,
                        "error", "Service Unavailable",
                        "message", "User service is temporarily unavailable"
                ));
    }

    @GetMapping("/cart")
    public ResponseEntity<Map<String, Object>> cartFallback() {

        return ResponseEntity
                .status(503)
                .body(Map.of(
                        "status", 503,
                        "error", "Service Unavailable",
                        "message", "Cart service is temporarily unavailable"
                ));
    }

    @GetMapping("/order")
    public ResponseEntity<Map<String, Object>> orderFallback() {

        return ResponseEntity
                .status(503)
                .body(Map.of(
                        "status", 503,
                        "error", "Service Unavailable",
                        "message", "Order service is temporarily unavailable"
                ));
    }

    @GetMapping("/payment")
    public ResponseEntity<Map<String, Object>> paymentFallback() {

        return ResponseEntity
                .status(503)
                .body(Map.of(
                        "status", 503,
                        "error", "Service Unavailable",
                        "message", "Payment service is temporarily unavailable"
                ));
    }

    @GetMapping("/otp")
    public ResponseEntity<Map<String, Object>> otpFallback() {

        return ResponseEntity
                .status(503)
                .body(Map.of(
                        "status", 503,
                        "error", "Service Unavailable",
                        "message", "OTP service is temporarily unavailable"
                ));
    }
}