package mvp_mart.order_service.service.impl;

import lombok.RequiredArgsConstructor;
import mvp_mart.order_service.dto.CreateOrderRequest;
import mvp_mart.order_service.dto.OrderItemRequest;
import mvp_mart.order_service.dto.OrderItemResponse;
import mvp_mart.order_service.dto.OrderResponse;
import mvp_mart.order_service.dto.UpdateOrderStatusRequest;
import mvp_mart.order_service.entity.Order;
import mvp_mart.order_service.entity.OrderItem;
import mvp_mart.order_service.enums.OrderStatus;
import mvp_mart.order_service.exception.OrderNotFoundException;
import mvp_mart.order_service.repository.OrderRepository;
import mvp_mart.order_service.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;


    // =========================================================
    // CREATE ORDER
    // =========================================================

    @Override
    public OrderResponse createOrder(
            Long userId,
            CreateOrderRequest request
    ) {

        Order order = Order.builder()
                .userId(userId)
                .shippingAddress(request.getShippingAddress())
                .status(OrderStatus.OTP_PENDING)
                .totalAmount(BigDecimal.ZERO)
                .build();

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (OrderItemRequest itemRequest : request.getItems()) {

            BigDecimal subtotal =
                    itemRequest.getUnitPrice()
                            .multiply(
                                    BigDecimal.valueOf(
                                            itemRequest.getQuantity()
                                    )
                            );

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .productId(itemRequest.getProductId())
                    .productName(itemRequest.getProductName())
                    .quantity(itemRequest.getQuantity())
                    .unitPrice(itemRequest.getUnitPrice())
                    .subtotal(subtotal)
                    .build();

            order.getItems().add(orderItem);

            totalAmount = totalAmount.add(subtotal);
        }

        order.setTotalAmount(totalAmount);

        Order savedOrder = orderRepository.save(order);

        return mapToResponse(savedOrder);
    }


    // =========================================================
    // GET ORDER BY ID
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(
                        () -> new OrderNotFoundException(orderId)
                );

        return mapToResponse(order);
    }


    // =========================================================
    // GET ORDERS BY USER
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersByUser(Long userId) {

        return orderRepository
                .findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    // =========================================================
    // GET ALL ORDERS
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrders() {

        return orderRepository
                .findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    // =========================================================
    // UPDATE ORDER STATUS
    // =========================================================

    @Override
    public OrderResponse updateOrderStatus(
            Long orderId,
            UpdateOrderStatusRequest request
    ) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(
                        () -> new OrderNotFoundException(orderId)
                );

        order.setStatus(request.getStatus());

        Order updatedOrder = orderRepository.save(order);

        return mapToResponse(updatedOrder);
    }


    // =========================================================
    // DELETE ORDER
    // =========================================================

    @Override
    public void deleteOrder(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(
                        () -> new OrderNotFoundException(orderId)
                );

        orderRepository.delete(order);
    }


    // =========================================================
    // MAP ENTITY TO RESPONSE
    // =========================================================

    private OrderResponse mapToResponse(Order order) {

        List<OrderItemResponse> itemResponses =
                order.getItems()
                        .stream()
                        .map(item ->
                                OrderItemResponse.builder()
                                        .id(item.getId())
                                        .productId(item.getProductId())
                                        .productName(item.getProductName())
                                        .quantity(item.getQuantity())
                                        .unitPrice(item.getUnitPrice())
                                        .subtotal(item.getSubtotal())
                                        .build()
                        )
                        .toList();

        return OrderResponse.builder()
                .id(order.getId())
                .userId(order.getUserId())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .shippingAddress(order.getShippingAddress())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .items(itemResponses)
                .build();
    }
}