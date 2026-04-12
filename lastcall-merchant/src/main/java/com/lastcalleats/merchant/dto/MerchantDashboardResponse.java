package com.lastcalleats.merchant.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

/**
 * 商家仪表盘响应 DTO，聚合多个模块的数据返回给前端。
 * 由 DashboardFacade 组装，包含今日订单数、今日收入、在售商品数。
 */
@Getter
@Builder
public class MerchantDashboardResponse {

    private Integer todayOrderCount;
    private BigDecimal todayRevenue;
    private Integer activeListingCount;
}
