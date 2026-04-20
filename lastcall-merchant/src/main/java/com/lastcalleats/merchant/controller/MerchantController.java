package com.lastcalleats.merchant.controller;

import com.lastcalleats.common.response.ApiResponse;
import com.lastcalleats.merchant.dto.MerchantDashboardResponse;
import com.lastcalleats.merchant.dto.MerchantProfileRequest;
import com.lastcalleats.merchant.dto.MerchantProfileResponse;
import com.lastcalleats.merchant.facade.DashboardFacade;
import com.lastcalleats.merchant.service.MerchantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for the merchant module.
 * Exposes endpoints for profile management, the public merchant page, and the dashboard.
 */
@RestController
@RequestMapping("/api/merchant")
@RequiredArgsConstructor
public class MerchantController {

    private final MerchantService merchantService;
    private final DashboardFacade dashboardFacade;

    @GetMapping("/profile")
    public ApiResponse<MerchantProfileResponse> getProfile() {
        Long merchantId = getCurrentMerchantId();
        return ApiResponse.success(merchantService.getProfile(merchantId));
    }

    @PutMapping("/profile")
    public ApiResponse<MerchantProfileResponse> updateProfile(
            @Valid @RequestBody MerchantProfileRequest request) {
        Long merchantId = getCurrentMerchantId();
        return ApiResponse.success(merchantService.updateProfile(merchantId, request));
    }

    @GetMapping("/dashboard")
    public ApiResponse<MerchantDashboardResponse> getDashboard() {
        Long merchantId = getCurrentMerchantId();
        return ApiResponse.success(dashboardFacade.getDashboard(merchantId));
    }

    @GetMapping("/{id}")
    public ApiResponse<MerchantProfileResponse> getPublicProfile(@PathVariable Long id) {
        return ApiResponse.success(merchantService.getPublicProfile(id));
    }

    // Extract the authenticated merchant ID from the SecurityContext
    private Long getCurrentMerchantId() {
        String merchantId = SecurityContextHolder.getContext().getAuthentication().getName();
        return Long.parseLong(merchantId);
    }
}
