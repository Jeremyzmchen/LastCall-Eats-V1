package com.lastcalleats.user.dto;

import lombok.Builder;
import lombok.Getter;

/** Response DTO for a user's profile; excludes sensitive fields such as password hash. */
@Getter
@Builder
public class UserProfileResponse {

    private Long id;
    private String email;
    private String nickname;
    private String avatarUrl;
}
