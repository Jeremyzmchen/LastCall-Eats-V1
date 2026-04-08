package com.lastcalleats.order.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CodeRequest {

    @NotBlank
    @Size(min = 6, max = 6, message = "Pickup code must be 6 digits")
    private String pickupCode;
}
