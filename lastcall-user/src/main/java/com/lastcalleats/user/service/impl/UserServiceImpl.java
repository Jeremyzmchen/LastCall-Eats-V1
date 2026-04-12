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

/**
 * 用户服务实现类，处理用户模块的核心业务逻辑。
 * 包括个人资料的查看、更新和头像上传。
 */
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
        // 调用策略接口上传文件，不关心具体存在哪里
        String avatarUrl = storageStrategy.upload(file, "avatars");
        user.setAvatarUrl(avatarUrl);
        userRepo.save(user);
        return toResponse(user);
    }

    /**
     * 根据 ID 查找用户，不存在则抛出异常。
     */
    private UserDO findUserById(Long userId) {
        return userRepo.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }

    /**
     * 将 Entity 转换为 DTO，避免重复代码。
     */
    private UserProfileResponse toResponse(UserDO user) {
        return UserProfileResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .avatarUrl(user.getAvatarUrl())
                .build();
    }
}
