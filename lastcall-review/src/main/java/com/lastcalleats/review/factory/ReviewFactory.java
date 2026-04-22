package com.lastcalleats.review.factory;

import com.lastcalleats.common.exception.BusinessException;
import com.lastcalleats.common.exception.ErrorCode;
import com.lastcalleats.common.util.Assert;
import com.lastcalleats.order.entity.OrderDO;
import com.lastcalleats.order.repository.OrderRepo;
import com.lastcalleats.product.entity.ProductListingDO;
import com.lastcalleats.product.repository.ProductListingRepo;
import com.lastcalleats.review.dto.CreateReviewRequest;
import com.lastcalleats.review.entity.ReviewDO;
import com.lastcalleats.review.repository.ReviewRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Factory responsible for validating review preconditions and assembling a
 * {@link ReviewDO} ready for persistence.
 * Keeping this logic out of the service layer makes the validation steps
 * independently testable without touching the database.
 */
@Component
@RequiredArgsConstructor
public class ReviewFactory {

    private final OrderRepo orderRepo;
    private final ProductListingRepo listingRepo;
    private final ReviewRepo reviewRepo;

    /**
     * Validates the review preconditions and builds a {@link ReviewDO}.
     * Checks in order: order exists → belongs to user → is COMPLETED → not yet reviewed.
     *
     * @param userId  the authenticated reviewer; must match the order's user ID
     * @param request the incoming review data from the client
     * @return a fully populated {@link ReviewDO} that has not yet been saved
     * @throws com.lastcalleats.common.exception.BusinessException for any
     *         failed precondition (see {@link com.lastcalleats.common.exception.ErrorCode})
     */
    public ReviewDO create(Long userId, CreateReviewRequest request) {
        OrderDO order = orderRepo.findById(request.getOrderId())
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));

        Assert.equals(order.getUserId(), userId, ErrorCode.FORBIDDEN);
        Assert.isTrue(order.getStatus() == OrderDO.OrderStatus.COMPLETED, ErrorCode.REVIEW_NOT_ALLOWED);
        Assert.isTrue(!reviewRepo.existsByOrderId(request.getOrderId()), ErrorCode.REVIEW_ALREADY_EXISTS);

        ProductListingDO listing = listingRepo.findById(order.getListingId())
                .orElseThrow(() -> new BusinessException(ErrorCode.LISTING_NOT_FOUND));

        return ReviewDO.builder()
                .orderId(order.getId())
                .userId(userId)
                .merchantId(order.getMerchantId())
                .templateId(listing.getTemplateId())
                .rating(request.getRating())
                .content(request.getContent())
                .imageUrls(request.getImageUrls())
                .build();
    }
}
