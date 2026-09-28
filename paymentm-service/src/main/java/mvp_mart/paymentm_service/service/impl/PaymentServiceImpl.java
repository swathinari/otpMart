package mvp_mart.paymentm_service.service.impl;

import lombok.RequiredArgsConstructor;
import mvp_mart.paymentm_service.dto.CreatePaymentRequest;
import mvp_mart.paymentm_service.dto.PaymentResponse;
import mvp_mart.paymentm_service.entity.Payment;
import mvp_mart.paymentm_service.enums.PaymentStatus;
import mvp_mart.paymentm_service.exception.PaymentNotFoundException;
import mvp_mart.paymentm_service.repository.PaymentRepository;
import mvp_mart.paymentm_service.service.PaymentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    @Override
    @Transactional
    public PaymentResponse createPayment(CreatePaymentRequest request) {

        Payment payment = Payment.builder()
                .orderId(request.getOrderId())
                .userId(request.getUserId())
                .amount(request.getAmount())
                .paymentMethod(request.getPaymentMethod())

                // Mock payment
                .status(PaymentStatus.SUCCESS)

                .transactionId(generateTransactionId())
                .build();

        Payment savedPayment = paymentRepository.save(payment);

        return mapToResponse(savedPayment);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponse getPaymentById(Long paymentId) {

        Payment payment = paymentRepository
                .findById(paymentId)
                .orElseThrow(
                        () -> new PaymentNotFoundException(paymentId)
                );

        return mapToResponse(payment);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponse getPaymentByOrderId(Long orderId) {

        Payment payment = paymentRepository
                .findByOrderId(orderId)
                .orElseThrow(
                        () -> new PaymentNotFoundException(orderId)
                );

        return mapToResponse(payment);
    }

    private String generateTransactionId() {

        return "TXN-" + UUID.randomUUID();
    }

    private PaymentResponse mapToResponse(Payment payment) {

        return PaymentResponse.builder()
                .paymentId(payment.getPaymentId())
                .orderId(payment.getOrderId())
                .userId(payment.getUserId())
                .amount(payment.getAmount())
                .status(payment.getStatus())
                .paymentMethod(payment.getPaymentMethod())
                .transactionId(payment.getTransactionId())
                .createdAt(payment.getCreatedAt())
                .updatedAt(payment.getUpdatedAt())
                .build();
    }
}