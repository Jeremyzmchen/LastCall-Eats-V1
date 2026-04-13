package com.lastcalleats.auth.dto;

import lombok.Data;

@Data
public class AuthResponse {
    private String accessToken;
    private String tokenType;
    private Long expiresIn;
    private Long userId;
    private String email;
    private String role;
}