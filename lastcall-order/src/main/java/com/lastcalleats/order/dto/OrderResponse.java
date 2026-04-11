package com.lastcalleats.order.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Response body for order APIs.
 */
@Getter
@Builder
public class OrderResponse {

    private Long id;
    private Long listingId;
    private Long merchantId;
    private String productName;
    private BigDecimal price;
    private String status;

    // Only returned after payment succeeds.
    private String pickupCode;

    // Added in V1: the raw QR string for the frontend.
    // The frontend should use a QR library to turn this string into a real QR image.
    private String qrCodeContent;

    private LocalDateTime createdAt;
}
