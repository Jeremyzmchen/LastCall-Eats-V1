package com.lastcalleats.user.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/** Response DTO for a favourited merchant entry. */
@Getter
@Builder
public class FavoriteMerchantResponse {

    private Long merchantId;
    private String merchantName;
    private String merchantAddress;
    private LocalDateTime favoritedAt;
}
