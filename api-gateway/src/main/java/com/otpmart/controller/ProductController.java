package com.otpmart.controller;

import com.otpmart.client.ProductServiceClient;
import com.otpmart.dto.ProductRequest;
import com.otpmart.dto.ProductResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/gateway/products")
public class ProductController {

    private final ProductServiceClient productServiceClient;

    public ProductController(ProductServiceClient productServiceClient) {
        this.productServiceClient = productServiceClient;
    }

    // ============================
    // CREATE PRODUCT
    // ============================

    @PostMapping
    @CircuitBreaker(
            name = "productServiceCircuitBreaker",
            fallbackMethod = "createProductFallback"
    )
    public ResponseEntity<ProductResponse> createProduct(
            @RequestBody ProductRequest request) {

        ProductResponse response =
                productServiceClient.createProduct(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // ============================
    // GET ALL PRODUCTS
    // ============================

    @GetMapping
    @CircuitBreaker(
            name = "productServiceCircuitBreaker",
            fallbackMethod = "getAllProductsFallback"
    )
    public ResponseEntity<List<ProductResponse>> getAllProducts() {

        List<ProductResponse> response =
                productServiceClient.getAllProducts();

        return ResponseEntity.ok(response);
    }

    // ============================
    // GET PRODUCT BY ID
    // ============================

    @GetMapping("/{id}")
    @CircuitBreaker(
            name = "productServiceCircuitBreaker",
            fallbackMethod = "getProductByIdFallback"
    )
    public ResponseEntity<ProductResponse> getProductById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                productServiceClient.getProductById(id)
        );
    }

    // ============================
    // UPDATE PRODUCT
    // ============================

    @PutMapping("/{id}")
    @CircuitBreaker(
            name = "productServiceCircuitBreaker",
            fallbackMethod = "updateProductFallback"
    )
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long id,
            @RequestBody ProductRequest request) {

        return ResponseEntity.ok(
                productServiceClient.updateProduct(id, request)
        );
    }

    // ============================
    // DELETE PRODUCT
    // ============================

    @DeleteMapping("/{id}")
    @CircuitBreaker(
            name = "productServiceCircuitBreaker",
            fallbackMethod = "deleteProductFallback"
    )
    public ResponseEntity<Void> deleteProduct(
            @PathVariable Long id) {

        productServiceClient.deleteProduct(id);

        return ResponseEntity.noContent().build();
    }


    // =====================================================
    // FALLBACK METHODS
    // =====================================================

    // CREATE fallback
    public ResponseEntity<Map<String, Object>> createProductFallback(
            ProductRequest request,
            Throwable throwable) {

        System.out.println(">>> CREATE PRODUCT FALLBACK <<<");
        System.out.println("Reason: " + throwable.getMessage());

        return productServiceUnavailable();
    }


    // GET ALL fallback
    public ResponseEntity<Map<String, Object>> getAllProductsFallback(
            Throwable throwable) {

        System.out.println(">>> GET ALL PRODUCTS FALLBACK <<<");
        System.out.println("Reason: " + throwable.getMessage());

        return productServiceUnavailable();
    }


    // GET BY ID fallback
    public ResponseEntity<Map<String, Object>> getProductByIdFallback(
            Long id,
            Throwable throwable) {

        System.out.println(">>> GET PRODUCT BY ID FALLBACK <<<");
        System.out.println("Product ID: " + id);
        System.out.println("Reason: " + throwable.getMessage());

        return productServiceUnavailable();
    }


    // UPDATE fallback
    public ResponseEntity<Map<String, Object>> updateProductFallback(
            Long id,
            ProductRequest request,
            Throwable throwable) {

        System.out.println(">>> UPDATE PRODUCT FALLBACK <<<");
        System.out.println("Product ID: " + id);
        System.out.println("Reason: " + throwable.getMessage());

        return productServiceUnavailable();
    }


    // DELETE fallback
    public ResponseEntity<Map<String, Object>> deleteProductFallback(
            Long id,
            Throwable throwable) {

        System.out.println(">>> DELETE PRODUCT FALLBACK <<<");
        System.out.println("Product ID: " + id);
        System.out.println("Reason: " + throwable.getMessage());

        return productServiceUnavailable();
    }


    // Common response
    private ResponseEntity<Map<String, Object>> productServiceUnavailable() {

        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of(
                        "status", 503,
                        "error", "Service Unavailable",
                        "message", "Product service is temporarily unavailable"
                ));
    }
}