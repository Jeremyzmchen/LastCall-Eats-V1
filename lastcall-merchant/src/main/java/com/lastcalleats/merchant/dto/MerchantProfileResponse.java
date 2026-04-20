package com.lastcalleats.merchant.dto;

import lombok.Builder;
import lombok.Getter;

/** Response DTO for a merchant's profile; excludes sensitive fields such as password hash. */
@Getter
@Builder
public class MerchantProfileResponse {

    private Long id;
    private String email;
    private String name;
    private String address;
    private String businessHours;
    private Boolean isActive;
}
