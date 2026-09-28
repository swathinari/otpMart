package mvp_mart.order_service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateOrderFromCartRequest {

    @NotBlank(message = "Shipping address is required")
    private String shippingAddress;
}