package com.lastcalleats.product.repository;

import com.lastcalleats.product.entity.ProductTemplateDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductTemplateRepo extends JpaRepository<ProductTemplateDO, Long> {

    List<ProductTemplateDO> findByMerchantId(Long merchantId);
}
