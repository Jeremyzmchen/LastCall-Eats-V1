package com.lastcalleats.product.provider;

import com.lastcalleats.common.provider.ListingStatsProvider;
import com.lastcalleats.product.repository.ProductListingRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Implementation of {@link com.lastcalleats.common.provider.ListingStatsProvider} in the product module.
 * Supplies the active listing count to the merchant dashboard without creating
 * a direct merchant → product module dependency.
 */
@Component
@RequiredArgsConstructor
public class ListingStatsProviderImpl implements ListingStatsProvider {

    private final ProductListingRepo productListingRepo;

    @Override
    public int getActiveListingCount(Long merchantId) {
        return productListingRepo.findByMerchantIdAndIsAvailableTrue(merchantId).size();
    }
}
