package com.lastcalleats.product.repository;

import com.lastcalleats.product.entity.ProductListingDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for product listing database operations.
 * This repository provides CRUD operations for {@link ProductListingDO}
 * and custom query methods for retrieving listings by merchant,
 * availability, and creation time.
 */
@Repository
public interface ProductListingRepo extends JpaRepository<ProductListingDO, Long> {

    List<ProductListingDO> findByMerchantId(Long merchantId);

    List<ProductListingDO> findByMerchantIdAndIsAvailableTrue(Long merchantId);

    List<ProductListingDO> findAllByOrderByCreatedAtDesc();
}
