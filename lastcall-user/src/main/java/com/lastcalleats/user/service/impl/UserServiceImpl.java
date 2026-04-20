package com.lastcalleats.user.service.impl;

import com.lastcalleats.common.exception.BusinessException;
import com.lastcalleats.common.exception.ErrorCode;
import com.lastcalleats.common.storage.StorageStrategy;
import com.lastcalleats.user.dto.UserProfileRequest;
import com.lastcalleats.user.dto.UserProfileResponse;
import com.lastcalleats.user.entity.UserDO;
import com.lastcalleats.user.repository.UserRepo;
import com.lastcalleats.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/** Implementation of UserService. */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final StorageStrategy storageStrategy;

    @Override
    public UserProfileResponse getProfile(Long userId) {
        UserDO user = findUserById(userId);
        return toResponse(user);
    }

    @Override
    public UserProfileResponse updateProfile(Long userId, UserProfileRequest request) {
        UserDO user = findUserById(userId);
        user.setNickname(request.getNickname());
        userRepo.save(user);
        return toResponse(user);
    }

    @Override
    public UserProfileResponse uploadAvatar(Long userId, MultipartFile file) {
        UserDO user = findUserById(userId);
        // Delegate to the storage strategy — implementation details are irrelevant here
        String avatarUrl = storageStrategy.upload(file, "avatars");
        user.setAvatarUrl(avatarUrl);
        userRepo.save(user);
        return toResponse(user);
    }

    // Fetch user by ID; throw BusinessException if not found
    private UserDO findUserById(Long userId) {
        return userRepo.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }

    // Map UserDO to response DTO
    private UserProfileResponse toResponse(UserDO user) {
        return UserProfileResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .avatarUrl(user.getAvatarUrl())
                .build();
    }
}
