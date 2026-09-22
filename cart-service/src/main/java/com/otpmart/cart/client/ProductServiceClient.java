package com.otpmart.cart.client;

import com.otpmart.cart.dto.ProductDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ProductServiceClient {

    private final RestTemplate restTemplate;

    @Value("${product-service.base-url}")
    private String productServiceBaseUrl;

    public ProductServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ProductDto getProductById(String productId) {
        String url = productServiceBaseUrl + "/api/products/" + productId;
        return restTemplate.getForObject(url, ProductDto.class);
    }
}