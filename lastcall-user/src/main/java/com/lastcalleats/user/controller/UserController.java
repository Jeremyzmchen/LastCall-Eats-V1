package com.lastcalleats.user.controller;

import com.lastcalleats.common.response.ApiResponse;
import com.lastcalleats.user.dto.FavoriteMerchantResponse;
import com.lastcalleats.user.dto.UserProfileRequest;
import com.lastcalleats.user.dto.UserProfileResponse;
import com.lastcalleats.user.service.FavoriteService;
import com.lastcalleats.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 用户控制器，处理用户模块的 HTTP 请求。
 * 提供个人资料查看、更新、头像上传和收藏管理接口。
 */
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final FavoriteService favoriteService;

    @GetMapping("/profile")
    public ApiResponse<UserProfileResponse> getProfile() {
        Long userId = getCurrentUserId();
        return ApiResponse.success(userService.getProfile(userId));
    }

    @PutMapping("/profile")
    public ApiResponse<UserProfileResponse> updateProfile(
            @Valid @RequestBody UserProfileRequest request) {
        Long userId = getCurrentUserId();
        return ApiResponse.success(userService.updateProfile(userId, request));
    }

    @PostMapping("/avatar")
    public ApiResponse<UserProfileResponse> uploadAvatar(
            @RequestParam("file") MultipartFile file) {
        Long userId = getCurrentUserId();
        return ApiResponse.success(userService.uploadAvatar(userId, file));
    }

    @GetMapping("/favorites")
    public ApiResponse<List<FavoriteMerchantResponse>> listFavorites() {
        Long userId = getCurrentUserId();
        return ApiResponse.success(favoriteService.listFavorites(userId));
    }

    @PostMapping("/favorites/{merchantId}")
    public ApiResponse<Void> addFavorite(@PathVariable Long merchantId) {
        Long userId = getCurrentUserId();
        favoriteService.addFavorite(userId, merchantId);
        return ApiResponse.success();
    }

    @DeleteMapping("/favorites/{merchantId}")
    public ApiResponse<Void> removeFavorite(@PathVariable Long merchantId) {
        Long userId = getCurrentUserId();
        favoriteService.removeFavorite(userId, merchantId);
        return ApiResponse.success();
    }

    @GetMapping("/favorites/{merchantId}")
    public ApiResponse<Boolean> isFavorite(@PathVariable Long merchantId) {
        Long userId = getCurrentUserId();
        return ApiResponse.success(favoriteService.isFavorite(userId, merchantId));
    }

    /**
     * 从 SecurityContextHolder 获取当前登录用户的 ID。
     */
    private Long getCurrentUserId() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        return Long.parseLong(userId);
    }
}
