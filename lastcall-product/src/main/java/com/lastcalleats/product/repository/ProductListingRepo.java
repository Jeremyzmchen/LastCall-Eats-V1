package com.lastcalleats.product.repository;

import com.lastcalleats.product.entity.ProductListingDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductListingRepo extends JpaRepository<ProductListingDO, Long> {

    List<ProductListingDO> findByMerchantId(Long merchantId);

    List<ProductListingDO> findByMerchantIdAndIsAvailableTrue(Long merchantId);

    List<ProductListingDO> findAllByOrderByCreatedAtDesc();
}
