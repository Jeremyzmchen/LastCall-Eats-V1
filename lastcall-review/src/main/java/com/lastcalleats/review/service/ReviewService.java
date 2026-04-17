package com.lastcalleats.review.service;

import com.lastcalleats.review.dto.CreateReviewRequest;
import com.lastcalleats.review.dto.ReviewResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ReviewService {

    /** Order must be COMPLETED and not already reviewed. */
    ReviewResponse createReview(Long userId, CreateReviewRequest request);

    /** Returns empty when the order has not been reviewed yet. */
    Optional<ReviewResponse> getReviewByOrder(Long orderId);

    Page<ReviewResponse> listReviewsByMerchant(Long merchantId, Pageable pageable);

    Page<ReviewResponse> listReviewsByTemplate(Long templateId, Pageable pageable);
}
