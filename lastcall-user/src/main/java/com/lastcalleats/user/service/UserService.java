package com.lastcalleats.user.service;

import com.lastcalleats.user.dto.UserProfileRequest;
import com.lastcalleats.user.dto.UserProfileResponse;
import org.springframework.web.multipart.MultipartFile;

/** Service interface for user-related operations. */
public interface UserService {

    UserProfileResponse getProfile(Long userId);

    UserProfileResponse updateProfile(Long userId, UserProfileRequest request);

    // V2 reserved: changePassword

    UserProfileResponse uploadAvatar(Long userId, MultipartFile file);
}
