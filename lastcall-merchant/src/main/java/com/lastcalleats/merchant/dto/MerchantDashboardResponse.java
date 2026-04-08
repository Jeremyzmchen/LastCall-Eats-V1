package com.lastcalleats.merchant.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class MerchantDashboardResponse {

    private Integer todayOrderCount;
    private BigDecimal todayRevenue;
    private Integer activeListingCount;
}
