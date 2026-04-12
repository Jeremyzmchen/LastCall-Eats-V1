package com.lastcalleats.user.service;

import com.lastcalleats.user.dto.UserProfileRequest;
import com.lastcalleats.user.dto.UserProfileResponse;
import org.springframework.web.multipart.MultipartFile;

/**
 * 用户服务接口，定义用户模块的所有业务操作。
 * 包括个人资料管理和头像上传功能。
 */
public interface UserService {

    UserProfileResponse getProfile(Long userId);

    UserProfileResponse updateProfile(Long userId, UserProfileRequest request);

    // V2 预留：changePassword

    UserProfileResponse uploadAvatar(Long userId, MultipartFile file);
}
