package com.lastcalleats.user.controller;

import com.lastcalleats.common.response.ApiResponse;
import com.lastcalleats.user.dto.FavoriteListingResponse;
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
 * REST controller for the user module.
 * Exposes endpoints for profile management, avatar upload, and favourite listings.
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
    public ApiResponse<List<FavoriteListingResponse>> listFavorites() {
        Long userId = getCurrentUserId();
        return ApiResponse.success(favoriteService.listFavorites(userId));
    }

    @PostMapping("/favorites/{listingId}")
    public ApiResponse<Void> addFavorite(@PathVariable Long listingId) {
        Long userId = getCurrentUserId();
        favoriteService.addFavorite(userId, listingId);
        return ApiResponse.success();
    }

    @DeleteMapping("/favorites/{listingId}")
    public ApiResponse<Void> removeFavorite(@PathVariable Long listingId) {
        Long userId = getCurrentUserId();
        favoriteService.removeFavorite(userId, listingId);
        return ApiResponse.success();
    }

    @GetMapping("/favorites/{listingId}")
    public ApiResponse<Boolean> isFavorite(@PathVariable Long listingId) {
        Long userId = getCurrentUserId();
        return ApiResponse.success(favoriteService.isFavorite(userId, listingId));
    }

    // Extract the authenticated user ID from the SecurityContext
    private Long getCurrentUserId() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        return Long.parseLong(userId);
    }
}
