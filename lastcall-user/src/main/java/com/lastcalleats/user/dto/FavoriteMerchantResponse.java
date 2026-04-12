package com.lastcalleats.user.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 收藏商家响应 DTO，用于用户查看收藏列表时返回。
 * 包含商家的基本信息和收藏时间，不包含商家的敏感数据。
 */
@Getter
@Builder
public class FavoriteMerchantResponse {

    private Long merchantId;
    private String merchantName;
    private String merchantAddress;
    private LocalDateTime favoritedAt;
}
