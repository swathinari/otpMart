package mvp_mart.paymentm_service.contoller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mvp_mart.paymentm_service.dto.CreatePaymentRequest;
import mvp_mart.paymentm_service.dto.PaymentResponse;
import mvp_mart.paymentm_service.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(
            @Valid @RequestBody CreatePaymentRequest request) {

        PaymentResponse response =
                paymentService.createPayment(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentResponse> getPaymentById(
            @PathVariable Long paymentId) {

        PaymentResponse response =
                paymentService.getPaymentById(paymentId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<PaymentResponse> getPaymentByOrderId(
            @PathVariable Long orderId) {

        PaymentResponse response =
                paymentService.getPaymentByOrderId(orderId);

        return ResponseEntity.ok(response);
    }
}