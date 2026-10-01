package mvp_mart.paymentm_service.exception;



public class PaymentNotFoundException extends RuntimeException {

    public PaymentNotFoundException(Long paymentId) {
        super("Payment not found with id: " + paymentId);
    }
}