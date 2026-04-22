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
 * Default implementation of {@link ReviewService}.
 * Business rule validation (order ownership, completion status, duplicate check)
 * is handled by {@link com.lastcalleats.review.factory.ReviewFactory}; this class
 * is responsible only for persistence and read queries.
 */
@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepo reviewRepo;
    private final ReviewFactory reviewFactory;

    /** {@inheritDoc} */
    @Override
    @Transactional
    public ReviewResponse createReview(Long userId, CreateReviewRequest request) {
        ReviewDO review = reviewFactory.create(userId, request);
        reviewRepo.save(review);
        return toResponse(review);
    }

    /** {@inheritDoc} */
    @Override
    public Optional<ReviewResponse> getReviewByOrder(Long orderId) {
        return reviewRepo.findByOrderId(orderId)
                .map(this::toResponse);
    }

    /** {@inheritDoc} */
    @Override
    public Page<ReviewResponse> listReviewsByMerchant(Long merchantId, Pageable pageable) {
        return reviewRepo.findByMerchantIdAndIsVisibleTrueOrderByCreatedAtDesc(merchantId, pageable)
                .map(this::toResponse);
    }

    /** {@inheritDoc} */
    @Override
    public Page<ReviewResponse> listReviewsByTemplate(Long templateId, Pageable pageable) {
        return reviewRepo.findByTemplateIdAndIsVisibleTrueOrderByCreatedAtDesc(templateId, pageable)
                .map(this::toResponse);
    }

    // userNickname / userAvatarUrl left null; frontend fetches them via the user API
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
