package mvp_mart.paymentm_service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import mvp_mart.paymentm_service.enums.PaymentMethod;
import mvp_mart.paymentm_service.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class PaymentResponse {

    private Long paymentId;

    private Long orderId;

    private Long userId;

    private BigDecimal amount;

    private PaymentStatus status;

    private PaymentMethod paymentMethod;

    private String transactionId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}