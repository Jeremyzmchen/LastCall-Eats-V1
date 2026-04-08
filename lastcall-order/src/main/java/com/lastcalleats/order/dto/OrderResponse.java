package com.lastcalleats.order.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class OrderResponse {

    private Long id;
    private Long listingId;
    private Long merchantId;
    private String productName;
    private BigDecimal price;
    private String status;

    // 仅支付成功后返回
    private String pickupCode;

    private LocalDateTime createdAt;
}
