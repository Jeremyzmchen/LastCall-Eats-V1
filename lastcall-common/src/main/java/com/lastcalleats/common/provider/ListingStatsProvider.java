package com.lastcalleats.common.provider;

/**
 * 商品上架统计数据提供接口，定义在 common 模块中。
 * 由 product 模块实现，merchant 模块的 DashboardFacade 通过此接口获取数据。
 * 这样 merchant 不需要直接依赖 product 模块，避免循环依赖。
 */
public interface ListingStatsProvider {

    int getActiveListingCount(Long merchantId);
}
