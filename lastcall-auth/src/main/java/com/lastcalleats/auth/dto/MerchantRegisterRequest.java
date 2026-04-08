package com.lastcalleats.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MerchantRegisterRequest {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
    private String password;

    @NotBlank
    @Size(min = 2, max = 100, message = "Business name must be between 2 and 100 characters")
    private String name;

    @NotBlank
    @Size(max = 500)
    private String address;
}
