package mvp_mart.order_service.service;



import mvp_mart.order_service.dto.CreateOrderRequest;
import mvp_mart.order_service.dto.OrderResponse;
import mvp_mart.order_service.dto.UpdateOrderStatusRequest;

import java.util.List;

public interface OrderService {

    OrderResponse createOrder(
            Long userId,
            CreateOrderRequest request
    );

    OrderResponse getOrderById(
            Long orderId
    );

    List<OrderResponse> getOrdersByUser(
            Long userId
    );

    List<OrderResponse> getAllOrders();

    OrderResponse updateOrderStatus(
            Long orderId,
            UpdateOrderStatusRequest request
    );

    void deleteOrder(
            Long orderId
    );
}