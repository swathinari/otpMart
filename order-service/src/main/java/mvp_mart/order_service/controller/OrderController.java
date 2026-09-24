package mvp_mart.order_service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mvp_mart.order_service.dto.CreateOrderRequest;
import mvp_mart.order_service.dto.OrderResponse;
import mvp_mart.order_service.dto.UpdateOrderStatusRequest;
import mvp_mart.order_service.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;


    // =========================================================
    // CREATE ORDER
    // =========================================================

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody CreateOrderRequest request
    ) {

        OrderResponse response =
                orderService.createOrder(
                        userId,
                        request
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


    // =========================================================
    // GET ORDER BY ID
    // =========================================================

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderById(
            @PathVariable Long orderId
    ) {

        OrderResponse response =
                orderService.getOrderById(orderId);

        return ResponseEntity.ok(response);
    }


    // =========================================================
    // GET MY ORDERS
    // =========================================================

    @GetMapping("/my-orders")
    public ResponseEntity<List<OrderResponse>> getMyOrders(
            @RequestHeader("X-User-Id") Long userId
    ) {

        List<OrderResponse> orders =
                orderService.getOrdersByUser(userId);

        return ResponseEntity.ok(orders);
    }


    // =========================================================
    // GET ALL ORDERS
    // =========================================================

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders() {

        List<OrderResponse> orders =
                orderService.getAllOrders();

        return ResponseEntity.ok(orders);
    }


    // =========================================================
    // UPDATE ORDER STATUS
    // =========================================================

    @PatchMapping("/{orderId}/status")
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @PathVariable Long orderId,
            @Valid @RequestBody UpdateOrderStatusRequest request
    ) {

        OrderResponse response =
                orderService.updateOrderStatus(
                        orderId,
                        request
                );

        return ResponseEntity.ok(response);
    }


    // =========================================================
    // DELETE ORDER
    // =========================================================

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrder(
            @PathVariable Long orderId
    ) {

        orderService.deleteOrder(orderId);

        return ResponseEntity
                .noContent()
                .build();
    }
}