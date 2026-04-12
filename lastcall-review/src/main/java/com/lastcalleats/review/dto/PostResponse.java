package com.lastcalleats.review.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 帖子响应 DTO，返回给前端展示帖子详情或列表。
 */
@Getter
@Builder
public class PostResponse {

    private Long id;
    private Long userId;
    private String userNickname;
    private String userAvatarUrl;
    private Long merchantId;
    private String merchantName;
    private String content;
    private List<String> imageUrls;
    private Integer likeCount;
    private Integer commentCount;
    private LocalDateTime createdAt;
}
