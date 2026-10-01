package mvp_mart.order_service.client;

import mvp_mart.order_service.dto.CreatePaymentRequest;
import mvp_mart.order_service.dto.PaymentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "payment-service",
        url = "${payment-service.base-url}"
)
public interface PaymentServiceClient {

    @PostMapping("/api/payments")
    PaymentResponse createPayment(
            @RequestBody CreatePaymentRequest request
    );
}