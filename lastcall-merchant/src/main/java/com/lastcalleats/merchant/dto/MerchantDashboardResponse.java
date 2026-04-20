package com.lastcalleats.merchant.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

/** Response DTO for the merchant dashboard; aggregates today's order count, revenue, and active listing count. */
@Getter
@Builder
public class MerchantDashboardResponse {

    private Integer todayOrderCount;
    private BigDecimal todayRevenue;
    private Integer activeListingCount;
}
