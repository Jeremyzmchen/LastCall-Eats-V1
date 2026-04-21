package com.lastcalleats.product.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalTime;

/**
 * Response DTO used when users browse available listings.
 *
 * This DTO contains listing information combined with merchant
 * and product details, designed for the user-facing browse page.
 */
@Getter
@Builder
public class UserBrowseResponse {

    private Long listingId;
    private Long templateId;
    private Long merchantId;
    private String merchantName;
    private String merchantAddress;
    private String productName;
    private String description;
    private BigDecimal originalPrice;
    private BigDecimal discountPrice;
    private Integer remainingQuantity;
    private LocalTime pickupStart;
    private LocalTime pickupEnd;
}
