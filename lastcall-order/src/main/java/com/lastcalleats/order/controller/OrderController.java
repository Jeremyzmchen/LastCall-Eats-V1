package com.lastcalleats.order.controller;

import com.lastcalleats.common.response.ApiResponse;
import com.lastcalleats.common.security.IdentityResolver;
import com.lastcalleats.order.dto.OrderRequest;
import com.lastcalleats.order.dto.OrderResponse;
import com.lastcalleats.order.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    private final IdentityResolver identityResolver;

    public OrderController(OrderService orderService, IdentityResolver identityResolver) {
        this.orderService = orderService;
        this.identityResolver = identityResolver;
    }

    /**
     * Creates a new order.
     *
     * @param httpRequest current request
     * @param orderRequest order creation payload
     * @return created order
     */
    @PostMapping("/orders")
    public ApiResponse<OrderResponse> createOrder(HttpServletRequest httpRequest,
                                                  @Valid @RequestBody OrderRequest orderRequest) {
        Long userId = identityResolver.resolveCurrentUserId(httpRequest);
        return ApiResponse.success(orderService.createOrder(userId, orderRequest));
    }

    /**
     * Gets all orders for the current user.
     *
     * @param httpRequest current request
     * @return order list
     */
    @GetMapping("/orders")
    public ApiResponse<List<OrderResponse>> getUserOrders(HttpServletRequest httpRequest) {
        Long userId = identityResolver.resolveCurrentUserId(httpRequest);
        return ApiResponse.success(orderService.getUserOrders(userId));
    }

    /**
     * Gets one order for the current user.
     *
     * @param httpRequest current request
     * @param id order ID
     * @return order detail
     */
    @GetMapping("/orders/{id}")
    public ApiResponse<OrderResponse> getUserOrderDetail(HttpServletRequest httpRequest,
                                                         @PathVariable Long id) {
        Long userId = identityResolver.resolveCurrentUserId(httpRequest);
        return ApiResponse.success(orderService.getUserOrderDetail(userId, id));
    }

    /**
     * Gets pickup info for a paid order.
     *
     * @param httpRequest current request
     * @param id order ID
     * @return order with pickup info
     */
    @GetMapping("/orders/{id}/pickup-code")
    // Added in V1: this endpoint also returns qrCodeContent.
    public ApiResponse<OrderResponse> getPickupCode(HttpServletRequest httpRequest,
                                                    @PathVariable Long id) {
        Long userId = identityResolver.resolveCurrentUserId(httpRequest);
        return ApiResponse.success(orderService.getPickupCode(userId, id));
    }

    /**
     * Gets all orders for the current merchant.
     *
     * @param httpRequest current request
     * @return merchant order list
     */
    @GetMapping("/merchant/orders")
    public ApiResponse<List<OrderResponse>> getMerchantOrders(HttpServletRequest httpRequest) {
        Long merchantId = identityResolver.resolveCurrentMerchantId(httpRequest);
        return ApiResponse.success(orderService.getMerchantOrders(merchantId));
    }
}
