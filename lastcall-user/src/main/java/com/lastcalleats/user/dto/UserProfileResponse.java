package com.lastcalleats.user.dto;

import lombok.Builder;
import lombok.Getter;

/**
 * 用户个人资料响应 DTO，返回给前端展示。
 * 不包含密码等敏感信息，只暴露用户可见的字段。
 */
@Getter
@Builder
public class UserProfileResponse {

    private Long id;
    private String email;
    private String nickname;
    private String avatarUrl;
}
