package com.lastcalleats.product.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalTime;

/**
 * 用户端浏览商品列表时的响应体。
 * 聚合了 Listing + Template + Merchant 的必要信息，避免前端多次请求。
 */
@Getter
@Builder
public class UserBrowseResponse {

    private Long listingId;
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
