package com.lastcalleats.review.repository;

import com.lastcalleats.review.entity.ReviewDO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 评价数据访问层，继承 JpaRepository 获得基础 CRUD 能力。
 * 提供按订单、商家、商品模板查询评价的方法。
 */
@Repository
public interface ReviewRepo extends JpaRepository<ReviewDO, Long> {

    /** 根据订单 ID 查询评价内容，用于展示该订单对应的评价详情。 */
    Optional<ReviewDO> findByOrderId(Long orderId);

    /** 判断某订单是否已存在评价，提交评价前做去重校验，不拿内容只判断有无。 */
    boolean existsByOrderId(Long orderId);

    /** 查询某商家的所有可见评价，按时间倒序分页，用于商家主页和 Discovery modal。 */
    Page<ReviewDO> findByMerchantIdAndIsVisibleTrueOrderByCreatedAtDesc(Long merchantId, Pageable pageable);

    /** 查询某商品模板的所有可见评价，按时间倒序分页，用于 Discovery 卡片详情。 */
    Page<ReviewDO> findByTemplateIdAndIsVisibleTrueOrderByCreatedAtDesc(Long templateId, Pageable pageable);
}
