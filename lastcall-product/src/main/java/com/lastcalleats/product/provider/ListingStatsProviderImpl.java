package com.lastcalleats.product.provider;

import com.lastcalleats.common.provider.ListingStatsProvider;
import com.lastcalleats.product.repository.ProductListingRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 商品上架统计数据提供者实现类。
 * 实现 common 模块中定义的 ListingStatsProvider 接口，
 * 供 merchant 模块的 DashboardFacade 调用，避免模块间的循环依赖。
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
