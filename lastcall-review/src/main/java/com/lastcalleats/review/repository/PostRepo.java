package com.lastcalleats.review.repository;

import com.lastcalleats.review.entity.PostDO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 额外提供按用户、按商家分页查询的方法。
 */
@Repository
public interface PostRepo extends JpaRepository<PostDO, Long> {

    /** 查询某用户发布的所有可见帖子，按创建时间倒序分页。 */
    Page<PostDO> findByUserIdAndIsVisibleTrueOrderByCreatedAtDesc(Long userId, Pageable pageable);

    /** 查询某商家下的所有可见帖子，按创建时间倒序分页。 */
    Page<PostDO> findByMerchantIdAndIsVisibleTrueOrderByCreatedAtDesc(Long merchantId, Pageable pageable);

    /** 查询全部可见帖子（广场），按创建时间倒序分页。 */
    Page<PostDO> findByIsVisibleTrueOrderByCreatedAtDesc(Pageable pageable);
}
