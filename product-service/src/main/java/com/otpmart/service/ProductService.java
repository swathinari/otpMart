package com.otpmart.service;

import com.otpmart.entity.Product;
import com.otpmart.exception.ProductNotFoundException;
import com.otpmart.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Create Product
    public Product createProduct(Product product) {

        if (product.getAvailable() == null) {
            product.setAvailable(product.getQuantity() != null
                    && product.getQuantity() > 0);
        }

        return productRepository.save(product);
    }

    // Get All Products
    public List<Product> getAllProducts() {

        return productRepository.findAll();
    }

    // Get Product By ID
    public Product getProductById(Long id) {

        return productRepository.findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException("Product not found with id: " + id));
    }

    // Update Product
    public Product updateProduct(Long id, Product updatedProduct) {

        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Product not found with id: " + id));

        existingProduct.setName(updatedProduct.getName());
        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setCategory(updatedProduct.getCategory());
        existingProduct.setQuantity(updatedProduct.getQuantity());

        if (updatedProduct.getAvailable() != null) {
            existingProduct.setAvailable(updatedProduct.getAvailable());
        } else {
            existingProduct.setAvailable(
                    updatedProduct.getQuantity() != null
                            && updatedProduct.getQuantity() > 0
            );
        }

        return productRepository.save(existingProduct);
    }

    // Delete Product
    public void deleteProduct(Long id) {

        if (!productRepository.existsById(id)) {
            throw new RuntimeException(
                    "Product not found with id: " + id
            );
        }

        productRepository.deleteById(id);
    }
}