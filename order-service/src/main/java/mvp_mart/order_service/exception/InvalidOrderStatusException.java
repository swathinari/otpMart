package mvp_mart.order_service.exception;

public class InvalidOrderStatusException extends RuntimeException {
    public InvalidOrderStatusException(String message)
    {
        super(message);
    }
}
