package com.lastcalleats.review.service.impl;

import com.lastcalleats.review.dto.CreateReviewRequest;
import com.lastcalleats.review.dto.ReviewResponse;
import com.lastcalleats.review.entity.ReviewDO;
import com.lastcalleats.review.factory.ReviewFactory;
import com.lastcalleats.review.repository.ReviewRepo;
import com.lastcalleats.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * 评价服务实现类，处理评价模块的核心业务逻辑。
 * 实体创建逻辑委托给 ReviewFactory，本类只负责持久化和查询。
 */
@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepo reviewRepo;
    private final ReviewFactory reviewFactory;

    @Override
    @Transactional
    public ReviewResponse createReview(Long userId, CreateReviewRequest request) {
        ReviewDO review = reviewFactory.create(userId, request);
        reviewRepo.save(review);
        return toResponse(review);
    }

    @Override
    public Optional<ReviewResponse> getReviewByOrder(Long orderId) {
        return reviewRepo.findByOrderId(orderId)
                .map(this::toResponse);
    }

    @Override
    public Page<ReviewResponse> listReviewsByMerchant(Long merchantId, Pageable pageable) {
        return reviewRepo.findByMerchantIdAndIsVisibleTrueOrderByCreatedAtDesc(merchantId, pageable)
                .map(this::toResponse);
    }

    @Override
    public Page<ReviewResponse> listReviewsByTemplate(Long templateId, Pageable pageable) {
        return reviewRepo.findByTemplateIdAndIsVisibleTrueOrderByCreatedAtDesc(templateId, pageable)
                .map(this::toResponse);
    }

    /**
     * 将 ReviewDO 转换为响应 DTO。
     * userNickname 和 userAvatarUrl 暂填 null，前端通过 userId 自行调用用户接口获取。
     */
    private ReviewResponse toResponse(ReviewDO review) {
        return ReviewResponse.builder()
                .id(review.getId())
                .orderId(review.getOrderId())
                .userId(review.getUserId())
                .merchantId(review.getMerchantId())
                .templateId(review.getTemplateId())
                .rating(review.getRating())
                .content(review.getContent())
                .imageUrls(review.getImageUrls())
                .createdAt(review.getCreatedAt())
                .build();
    }
}
