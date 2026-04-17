package com.lastcalleats.user.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 收藏 listing 响应 DTO，用于用户查看收藏列表时返回。
 */
@Getter
@Builder
public class FavoriteListingResponse {

    private Long listingId;
    private Long merchantId;
    private String merchantName;
    private String productName;
    private String description;
    private BigDecimal originalPrice;
    private BigDecimal discountPrice;
    private Integer remainingQuantity;
    private LocalTime pickupStart;
    private LocalTime pickupEnd;
    private LocalDate date;
    private Boolean isAvailable;
    private LocalDateTime favoritedAt;
}