package mvp_mart.order_service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemResponse {

    private String productId;
    private String name;
    private Double price;
    private Integer quantity;
}