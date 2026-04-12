package com.lastcalleats.merchant.dto;

import lombok.Builder;
import lombok.Getter;

/**
 * 商家资料响应 DTO，返回给前端展示。
 * 不包含密码等敏感信息，只暴露商家可见的字段。
 */
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
