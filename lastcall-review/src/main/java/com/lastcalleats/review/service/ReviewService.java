package com.lastcalleats.review.service;

import com.lastcalleats.review.dto.CreateReviewRequest;
import com.lastcalleats.review.dto.ReviewResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * 评价服务接口，定义评价模块的所有业务操作。
 * Controller 只依赖此接口，不感知具体实现。
 */
public interface ReviewService {

    /**
     * 提交评价，只有订单状态为 COMPLETED 且未评价过的订单才能提交。
     *
     * @param userId  当前登录用户 ID
     * @param request 评价内容请求 DTO
     * @return 创建成功后的评价详情
     */
    ReviewResponse createReview(Long userId, CreateReviewRequest request);

    /**
     * 根据订单 ID 查询评价，用于 Binder 页面判断该订单是否已评价。
     * 未评价时返回 empty，前端据此决定显示"写评价"还是"查看评价"。
     *
     * @param orderId 订单 ID
     * @return 评价详情，不存在时返回 empty
     */
    Optional<ReviewResponse> getReviewByOrder(Long orderId);

    /**
     * 查询某商家的评价列表，按时间倒序分页，用于商家主页展示。
     *
     * @param merchantId 商家 ID
     * @param pageable   分页参数
     * @return 评价分页结果
     */
    Page<ReviewResponse> listReviewsByMerchant(Long merchantId, Pageable pageable);

    /**
     * 查询某商品模板的评价列表，按时间倒序分页，用于 Discovery modal 展示历史评价。
     *
     * @param templateId 商品模板 ID
     * @param pageable   分页参数
     * @return 评价分页结果
     */
    Page<ReviewResponse> listReviewsByTemplate(Long templateId, Pageable pageable);
}
