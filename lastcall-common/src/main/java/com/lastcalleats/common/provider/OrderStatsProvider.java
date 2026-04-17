package com.lastcalleats.common.provider;

import java.math.BigDecimal;

/** Defined in common to avoid a direct merchant → order dependency. */
public interface OrderStatsProvider {

    int getTodayOrderCount(Long merchantId);

    BigDecimal getTodayRevenue(Long merchantId);
}
