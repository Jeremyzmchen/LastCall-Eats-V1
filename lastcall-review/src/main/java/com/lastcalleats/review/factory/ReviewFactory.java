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
 * 评价工厂类（Factory Pattern），负责创建合法的 ReviewDO 实体。
 * 封装创建评价所需的全部业务校验和数据组装，ReviewService 不感知这些细节。
 */
@Component
@RequiredArgsConstructor
public class ReviewFactory {

    private final OrderRepo orderRepo;
    private final ProductListingRepo listingRepo;
    private final ReviewRepo reviewRepo;

    /**
     * 校验业务规则并组装 ReviewDO，不执行数据库写入。
     * 校验顺序：订单存在 → 订单归属当前用户 → 订单已完成 → 订单未被评价过。
     *
     * @param userId  当前登录用户 ID
     * @param request 评价请求 DTO
     * @return 组装好的 ReviewDO，由调用方负责持久化
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
