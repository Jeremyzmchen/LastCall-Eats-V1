package com.lastcalleats.review.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

/**
 * content 为必填项，图片和关联商家为可选。
 */
@Data
public class CreatePostRequest {

    @NotBlank(message = "Post content cannot be blank")
    @Size(max = 1000, message = "Post content cannot exceed 1000 characters")
    private String content;

    /** 关联商家 ID，可为空。 */
    private Long merchantId;

    /** 图片 URL 列表，由前端先上传到 MinIO 拿到 URL 后再随帖子一起提交。最多 9 张。 */
    @Size(max = 9, message = "Cannot attach more than 9 images")
    private List<String> imageUrls;
}
