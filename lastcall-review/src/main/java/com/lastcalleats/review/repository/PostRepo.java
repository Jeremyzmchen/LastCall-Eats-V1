package com.lastcalleats.review.repository;

import com.lastcalleats.review.entity.PostDO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepo extends JpaRepository<PostDO, Long> {

    Page<PostDO> findByUserIdAndIsVisibleTrueOrderByCreatedAtDesc(Long userId, Pageable pageable);

    Page<PostDO> findByMerchantIdAndIsVisibleTrueOrderByCreatedAtDesc(Long merchantId, Pageable pageable);

    Page<PostDO> findByIsVisibleTrueOrderByCreatedAtDesc(Pageable pageable);
}
