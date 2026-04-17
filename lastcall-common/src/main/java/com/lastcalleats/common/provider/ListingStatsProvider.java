package com.lastcalleats.common.provider;

/** Defined in common to avoid a direct merchant → product dependency. */
public interface ListingStatsProvider {

    int getActiveListingCount(Long merchantId);
}
