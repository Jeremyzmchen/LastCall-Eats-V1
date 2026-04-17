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

/** Validates business rules and assembles a ReviewDO; does not persist. */
@Component
@RequiredArgsConstructor
public class ReviewFactory {

    private final OrderRepo orderRepo;
    private final ProductListingRepo listingRepo;
    private final ReviewRepo reviewRepo;

    /** Checks order exists → belongs to user → is COMPLETED → not yet reviewed. */
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
