package com.lastcalleats.merchant.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/** Request DTO for updating a merchant's profile. */
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
