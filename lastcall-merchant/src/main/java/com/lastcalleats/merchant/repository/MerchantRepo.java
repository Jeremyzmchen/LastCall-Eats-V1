package com.lastcalleats.merchant.repository;

import com.lastcalleats.merchant.entity.MerchantDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/** Spring Data JPA repository for MerchantDO. */
@Repository
public interface MerchantRepo extends JpaRepository<MerchantDO, Long> {

    Optional<MerchantDO> findByEmail(String email);

    boolean existsByEmail(String email);
}
