package com.lastcalleats.order.service.impl;

import com.lastcalleats.common.exception.BusinessException;
import com.lastcalleats.common.exception.ErrorCode;
import com.lastcalleats.order.dto.OrderRequest;
import com.lastcalleats.order.dto.OrderResponse;
import com.lastcalleats.order.entity.OrderDO;
import com.lastcalleats.order.entity.PickupCodeDO;
import com.lastcalleats.order.factory.PickupCodeFactory;
import com.lastcalleats.order.repository.OrderRepo;
import com.lastcalleats.order.repository.PickupCodeRepo;
import com.lastcalleats.order.service.OrderService;
import com.lastcalleats.order.state.CancelledState;
import com.lastcalleats.order.state.CompletedState;
import com.lastcalleats.order.state.OrderState;
import com.lastcalleats.order.state.PaidState;
import com.lastcalleats.order.state.PendingPaymentState;
import com.lastcalleats.product.entity.ProductListingDO;
import com.lastcalleats.product.entity.ProductTemplateDO;
import com.lastcalleats.product.repository.ProductListingRepo;
import com.lastcalleats.product.repository.ProductTemplateRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Main implementation of {@link OrderService}.
 */
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepo orderRepo;
    private final PickupCodeRepo pickupCodeRepo;
    private final ProductListingRepo productListingRepo;
    private final ProductTemplateRepo productTemplateRepo;
    private final PickupCodeFactory pickupCodeFactory;

    public OrderServiceImpl(OrderRepo orderRepo,
                            PickupCodeRepo pickupCodeRepo,
                            ProductListingRepo productListingRepo,
                            ProductTemplateRepo productTemplateRepo,
                            PickupCodeFactory pickupCodeFactory) {
        this.orderRepo = orderRepo;
        this.pickupCodeRepo = pickupCodeRepo;
        this.productListingRepo = productListingRepo;
        this.productTemplateRepo = productTemplateRepo;
        this.pickupCodeFactory = pickupCodeFactory;
    }

    @Override
    @Transactional
    public OrderResponse createOrder(Long userId, OrderRequest request) {
        // Find the listing by listingId.
        ProductListingDO listing = productListingRepo.findById(request.getListingId())
                .orElseThrow(() -> new BusinessException(ErrorCode.LISTING_NOT_FOUND));

        // Check if the listing is available.
        if (!Boolean.TRUE.equals(listing.getIsAvailable())) {
            throw new BusinessException(ErrorCode.LISTING_NOT_AVAILABLE);
        }

        // Check if the listing is sold out.
        if (listing.getRemainingQuantity() == null || listing.getRemainingQuantity() <= 0) {
            throw new BusinessException(ErrorCode.LISTING_SOLD_OUT);
        }

        // Check if the order already exists.
        // One user can order the same listing only once per day.
        LocalDateTime start = LocalDate.now().atStartOfDay();
        LocalDateTime end = start.plusDays(1);
        if (orderRepo.existsByUserIdAndListingIdAndCreatedAtBetween(userId, listing.getId(), start, end)) {
            throw new BusinessException(ErrorCode.ORDER_ALREADY_EXISTS);
        }

        // Decrease quantity.
        listing.setRemainingQuantity(listing.getRemainingQuantity() - 1);
        if (listing.getRemainingQuantity() <= 0) {
            listing.setIsAvailable(false);
        }

        // Create the order.
        OrderDO order = OrderDO.builder()
                .userId(userId)
                .listingId(listing.getId())
                .merchantId(listing.getMerchantId())
                .price(listing.getDiscountPrice())
                .status(OrderDO.OrderStatus.PENDING_PAYMENT)
                .build();

        // Save the order.
        OrderDO saved = orderRepo.save(order);
        return toOrderResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getUserOrders(Long userId) {
        return orderRepo.findByUserId(userId).stream()
                .map(this::toOrderResponse)
                .toList();
    }


    @Override
    @Transactional(readOnly = true)
    public OrderResponse getUserOrderDetail(Long userId, Long orderId) {
        // The order must belong to the current user.
        OrderDO order = orderRepo.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));
        return toOrderResponse(order);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getPickupCode(Long userId, Long orderId) {
        // Ensure that the order belongs to the current user.
        OrderDO order = orderRepo.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));

        // Find the pickup code for this order.
        PickupCodeDO pickupCode = pickupCodeRepo.findByOrderId(order.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_STATUS_INVALID, "Pickup code is not available yet"));

        // The order must be in PAID status.
        if (order.getStatus() == OrderDO.OrderStatus.PENDING_PAYMENT) {
            throw new BusinessException(ErrorCode.ORDER_STATUS_INVALID, "Pickup code is not available yet");
        }

        // Return the order with pickup info.
        return toOrderResponse(order, pickupCode);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getMerchantOrders(Long merchantId) {
        return orderRepo.findByMerchantId(merchantId).stream()
                .map(this::toOrderResponse)
                .toList();
    }

    @Override
    @Transactional
    public OrderResponse markOrderPaid(Long orderId) {
        // Find the order.
        OrderDO order = orderRepo.findById(orderId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));

        // Find the state object and run the pay method.
        resolveState(order.getStatus()).pay(order);

        // Save the new order status.
        OrderDO saved = orderRepo.save(order);

        // Create pickup codes if they do not exist yet.
        if (pickupCodeRepo.findByOrderId(saved.getId()).isEmpty()) {
            pickupCodeRepo.save(PickupCodeDO.builder()
                    .orderId(saved.getId())
                    .numericCode(pickupCodeFactory.generate(saved, "NUMERIC"))
                    .qrCode(pickupCodeFactory.generate(saved, "QR"))
                    .used(false)
                    .build());
        }
        return toOrderResponse(saved);
    }

    /**
     * Builds an OrderResponse from an OrderDO object.
     *
     * @param order order entity
     * @return order response
     */
    private OrderResponse toOrderResponse(OrderDO order) {
        return toOrderResponse(order, pickupCodeRepo.findByOrderId(order.getId()).orElse(null));
    }

    /**
     * Builds an OrderResponse from an order and pickup code.
     *
     * @param order order entity
     * @param pickupCode pickup code entity, or {@code null} if not created yet
     * @return order response
     */
    private OrderResponse toOrderResponse(OrderDO order, PickupCodeDO pickupCode) {
        ProductListingDO listing = productListingRepo.findById(order.getListingId())
                .orElseThrow(() -> new BusinessException(ErrorCode.LISTING_NOT_FOUND));
        ProductTemplateDO template = productTemplateRepo.findById(listing.getTemplateId())
                .orElseThrow(() -> new BusinessException(ErrorCode.TEMPLATE_NOT_FOUND));

        return OrderResponse.builder()
                .id(order.getId())
                .listingId(order.getListingId())
                .merchantId(order.getMerchantId())
                .productName(template.getName())
                .price(order.getPrice())
                .status(order.getStatus().name())
                .pickupCode(pickupCode == null ? null : pickupCode.getNumericCode())
                .qrCodeContent(pickupCode == null ? null : pickupCode.getQrCode())
                .createdAt(order.getCreatedAt())
                .build();
    }

    /**
     * Finds the state object for the current order status.
     *
     * @param status order status
     * @return matching state object
     */
    private OrderState resolveState(OrderDO.OrderStatus status) {
        return switch (status) {
            case PENDING_PAYMENT -> new PendingPaymentState();
            case PAID -> new PaidState();
            case COMPLETED -> new CompletedState();
            case CANCELLED -> new CancelledState();
        };
    }
}
