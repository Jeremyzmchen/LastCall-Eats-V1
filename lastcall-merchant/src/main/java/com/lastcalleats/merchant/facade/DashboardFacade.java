package com.lastcalleats.merchant.facade;

import com.lastcalleats.common.provider.ListingStatsProvider;
import com.lastcalleats.common.provider.OrderStatsProvider;
import com.lastcalleats.merchant.dto.MerchantDashboardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 商家仪表盘门面类（Facade Pattern）。
 * 聚合来自多个模块的数据（订单统计、商品统计），封装成一个统一的响应。
 * Controller 只需调用 Facade 的一个方法，不需要知道数据来自哪些模块。
 */
@Component
@RequiredArgsConstructor
public class DashboardFacade {

    private final OrderStatsProvider orderStatsProvider;
    private final ListingStatsProvider listingStatsProvider;

    /**
     * 获取商家仪表盘数据，聚合订单和商品模块的统计信息。
     *
     * @param merchantId 商家 ID
     * @return 包含今日订单数、今日收入、在售商品数的响应
     */
    public MerchantDashboardResponse getDashboard(Long merchantId) {
        return MerchantDashboardResponse.builder()
                .todayOrderCount(orderStatsProvider.getTodayOrderCount(merchantId))
                .todayRevenue(orderStatsProvider.getTodayRevenue(merchantId))
                .activeListingCount(listingStatsProvider.getActiveListingCount(merchantId))
                .build();
    }
}
