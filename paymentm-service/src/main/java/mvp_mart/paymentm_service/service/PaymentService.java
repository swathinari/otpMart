package mvp_mart.paymentm_service.service;

import mvp_mart.paymentm_service.dto.CreatePaymentRequest;
import mvp_mart.paymentm_service.dto.PaymentResponse;

public interface PaymentService {

    PaymentResponse createPayment(CreatePaymentRequest request);

    PaymentResponse getPaymentById(Long paymentId);

    PaymentResponse getPaymentByOrderId(Long orderId);
}