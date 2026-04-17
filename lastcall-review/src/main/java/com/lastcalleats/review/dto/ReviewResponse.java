package com.lastcalleats.review.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

/** Excludes internal fields such as isVisible. */
@Getter
@Builder
public class ReviewResponse {

    private Long id;
    private Long orderId;
    private Long userId;
    private String userNickname;
    private String userAvatarUrl;
    private Long merchantId;
    private Long templateId;
    private Integer rating;
    private String content;
    private List<String> imageUrls;
    private LocalDateTime createdAt;
}
