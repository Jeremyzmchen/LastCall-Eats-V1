package com.lastcalleats.review.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

/**
 * 创建评价请求 DTO，承载用户提交评价时的数据。
 * orderId 和 rating 为必填，文字内容和图片为可选。
 */
@Data
public class CreateReviewRequest {

    @NotNull(message = "Order ID is required")
    private Long orderId;

    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private Integer rating;

    @Size(max = 500, message = "Review content cannot exceed 500 characters")
    private String content;

    /** 图片 URL 列表，前端先上传到 MinIO 拿到 URL 后随评价一起提交，最多 9 张。 */
    @Size(max = 9, message = "Cannot attach more than 9 images")
    private List<String> imageUrls;
}
