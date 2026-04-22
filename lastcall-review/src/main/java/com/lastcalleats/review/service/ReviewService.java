package com.lastcalleats.review.service;

import com.lastcalleats.review.dto.CreateReviewRequest;
import com.lastcalleats.review.dto.ReviewResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service contract for review operations.
 * A review is tied to a single completed order; enforcement of that constraint
 * is delegated to {@link com.lastcalleats.review.factory.ReviewFactory}.
 */
public interface ReviewService {

    /**
     * Creates a review for a completed order.
     * The order must be in COMPLETED status and must not already have a review.
     *
     * @param userId  the authenticated reviewer
     * @param request rating, optional text content, and optional image URLs
     * @return the saved review
     * @throws com.lastcalleats.common.exception.BusinessException if the order
     *         is not found, does not belong to the user, is not completed, or is
     *         already reviewed
     */
    ReviewResponse createReview(Long userId, CreateReviewRequest request);

    /**
     * Looks up the review for a given order.
     * Returns empty when the order has not been reviewed yet.
     *
     * @param orderId the order to look up
     * @return the review wrapped in an {@link Optional}, or empty if none exists
     */
    Optional<ReviewResponse> getReviewByOrder(Long orderId);

    /**
     * Returns visible reviews for the specified merchant, newest first.
     *
     * @param merchantId the merchant's ID
     * @param pageable   pagination parameters
     * @return a page of review responses
     */
    Page<ReviewResponse> listReviewsByMerchant(Long merchantId, Pageable pageable);

    /**
     * Returns visible reviews for the specified product template, newest first.
     * Used on the Discovery screen to show historical ratings per template.
     *
     * @param templateId the product template's ID
     * @param pageable   pagination parameters
     * @return a page of review responses
     */
    Page<ReviewResponse> listReviewsByTemplate(Long templateId, Pageable pageable);
}
