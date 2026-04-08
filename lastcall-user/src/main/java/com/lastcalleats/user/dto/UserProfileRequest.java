package com.lastcalleats.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserProfileRequest {

    @NotBlank
    @Size(min = 2, max = 50, message = "Nickname must be between 2 and 50 characters")
    private String nickname;
}
