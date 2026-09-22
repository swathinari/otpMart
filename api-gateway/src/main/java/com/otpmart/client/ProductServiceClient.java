package com.otpmart.client;

import com.otpmart.dto.ProductRequest;
import com.otpmart.dto.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(
        name = "product-service",
        url = "${product-service.url}"
)
public interface ProductServiceClient {

    @PostMapping("/api/products")
    ProductResponse createProduct(
            @RequestBody ProductRequest request
    );

    @GetMapping("/api/products")
    List<ProductResponse> getAllProducts();

    @GetMapping("/api/products/{id}")
    ProductResponse getProductById(
            @PathVariable("id") Long id
    );

    @PutMapping("/api/products/{id}")
    ProductResponse updateProduct(
            @PathVariable("id") Long id,
            @RequestBody ProductRequest request
    );

    @DeleteMapping("/api/products/{id}")
    void deleteProduct(
            @PathVariable("id") Long id
    );
}