package com.otpmart.controller;

import com.otpmart.entity.Product;
import com.otpmart.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // Create Product
    @PostMapping
    public ResponseEntity<Product> createProduct(
            @RequestBody @Valid Product product) {

        Product createdProduct =
                productService.createProduct(product);

        return new ResponseEntity<>(
                createdProduct,
                HttpStatus.CREATED
        );
    }

    // Get All Products
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {

        List<Product> products =
                productService.getAllProducts();

        return ResponseEntity.ok(products);
    }

    // Get Product By ID
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(
            @PathVariable Long id) {

        Product product =
                productService.getProductById(id);

        return ResponseEntity.ok(product);
    }

    // Update Product
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long id,
            @RequestBody Product product) {

        Product updatedProduct =
                productService.updateProduct(id, product);

        return ResponseEntity.ok(updatedProduct);
    }

    // Delete Product
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable Long id) {

        productService.deleteProduct(id);

        return ResponseEntity.noContent().build();
    }
}