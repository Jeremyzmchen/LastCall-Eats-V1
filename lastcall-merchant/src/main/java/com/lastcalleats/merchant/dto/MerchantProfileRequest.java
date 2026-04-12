package com.lastcalleats.merchant.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 商家资料更新请求 DTO，接收前端传来的商家信息。
 * 只包含商家可以自己修改的字段，不包含 id、email 等不可变字段。
 */
@Data
public class MerchantProfileRequest {

    @NotBlank
    @Size(min = 2, max = 100)
    private String name;

    @NotBlank
    @Size(max = 500)
    private String address;

    @Size(max = 100)
    private String businessHours;
}
