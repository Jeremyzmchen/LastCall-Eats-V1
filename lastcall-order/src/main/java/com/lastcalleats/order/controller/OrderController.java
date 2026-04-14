package com.lastcalleats.order.controller;

import com.lastcalleats.common.response.ApiResponse;
import com.lastcalleats.order.dto.OrderRequest;
import com.lastcalleats.order.dto.OrderResponse;
import com.lastcalleats.order.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller for order APIs.
 */
@RestController
@RequestMapping("/api")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Creates a new order.
     *
     * @param userId current user ID
     * @param orderRequest order creation payload
     * @return created order
     */
    @PostMapping("/orders")
    public ApiResponse<OrderResponse> createOrder(@RequestHeader("X-User-Id") Long userId,
                                                  @Valid @RequestBody OrderRequest orderRequest) {
        return ApiResponse.success(orderService.createOrder(userId, orderRequest));
    }

    /**
     * Gets all orders for the current user.
     *
     * @param userId current user ID
     * @return order list
     */
    @GetMapping("/orders")
    public ApiResponse<List<OrderResponse>> getUserOrders(@RequestHeader("X-User-Id") Long userId) {
        return ApiResponse.success(orderService.getUserOrders(userId));
    }

    /**
     * Gets one order for the current user.
     *
     * @param userId current user ID
     * @param id order ID
     * @return order detail
     */
    @GetMapping("/orders/{id}")
    public ApiResponse<OrderResponse> getUserOrderDetail(@RequestHeader("X-User-Id") Long userId,
                                                         @PathVariable Long id) {
        return ApiResponse.success(orderService.getUserOrderDetail(userId, id));
    }

    /**
     * Gets pickup info for a paid order.
     *
     * @param userId current user ID
     * @param id order ID
     * @return order with pickup info
     */
    @GetMapping("/orders/{id}/pickup-code")
    // Added in V1: this endpoint also returns qrCodeContent.
    public ApiResponse<OrderResponse> getPickupCode(@RequestHeader("X-User-Id") Long userId,
                                                    @PathVariable Long id) {
        return ApiResponse.success(orderService.getPickupCode(userId, id));
    }

    /**
     * Gets all orders for the current merchant.
     *
     * @param merchantId current merchant ID
     * @return merchant order list
     */
    @GetMapping("/merchant/orders")
    public ApiResponse<List<OrderResponse>> getMerchantOrders(@RequestHeader("X-Merchant-Id") Long merchantId) {
        return ApiResponse.success(orderService.getMerchantOrders(merchantId));
    }
}
