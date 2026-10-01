package mvp_mart.order_service.dto;

import lombok.Getter;
import lombok.Setter;
import mvp_mart.order_service.enums.PaymentMethod;

import java.math.BigDecimal;

@Getter
@Setter
public class CreatePaymentRequest {

    private Long orderId;

    private Long userId;

    private BigDecimal amount;

    private PaymentMethod paymentMethod;

    public CreatePaymentRequest() {
    }

    public CreatePaymentRequest(
            Long orderId,
            Long userId,
            BigDecimal amount,
            PaymentMethod paymentMethod) {

        this.orderId = orderId;
        this.userId = userId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
    }
}