package mvp_mart.order_service.client;

import mvp_mart.order_service.dto.CartItemResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class CartServiceClient {

    private final RestTemplate restTemplate;

    @Value("${cart-service.base-url}")
    private String cartServiceBaseUrl;

    public CartServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<CartItemResponse> getCartItems(Long userId) {

        String url = cartServiceBaseUrl
                + "/api/cart/"
                + userId
                + "/items";

        ResponseEntity<List<CartItemResponse>> response =
                restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<CartItemResponse>>() {}
                );

        return response.getBody();
    }

    public void clearCart(Long userId) {

        String url = cartServiceBaseUrl
                + "/api/cart/"
                + userId;

        restTemplate.delete(url);
    }
}