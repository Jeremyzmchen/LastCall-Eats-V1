package com.lastcalleats.common.provider;

import java.math.BigDecimal;

/**
 * 订单统计数据提供接口，定义在 common 模块中。
 * 由 order 模块实现，merchant 模块的 DashboardFacade 通过此接口获取数据。
 * 这样 merchant 不需要直接依赖 order 模块，避免循环依赖。
 */
public interface OrderStatsProvider {

    int getTodayOrderCount(Long merchantId);

    BigDecimal getTodayRevenue(Long merchantId);
}
