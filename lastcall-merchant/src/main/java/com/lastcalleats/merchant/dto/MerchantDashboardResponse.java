package com.lastcalleats.merchant.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

/**
 * 商家首页 Dashboard 数据。
 * V1 仅展示基础统计，不含评分（review 模块 V1 不开发）。
 */
@Getter
@Builder
public class MerchantDashboardResponse {

    // 今日已售订单数
    private Integer todayOrderCount;

    // 今日营业额
    private BigDecimal todayRevenue;

    // 当前有效 Listing 数量
    private Integer activeListingCount;
}
