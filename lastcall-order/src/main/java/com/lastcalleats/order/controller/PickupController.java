package com.lastcalleats.order.controller;

import com.lastcalleats.common.response.ApiResponse;
import com.lastcalleats.order.dto.CodeRequest;
import com.lastcalleats.order.dto.CodeResponse;
import com.lastcalleats.order.service.PickupCodeService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for merchant pickup verification APIs.
 */
@RestController
@RequestMapping("/api/merchant/orders")
public class PickupController {

    private final PickupCodeService pickupCodeService;

    public PickupController(PickupCodeService pickupCodeService) {
        this.pickupCodeService = pickupCodeService;
    }

    /**
     * Verifies a pickup code from a merchant.
     *
     * @param merchantId current merchant
     * @param codeRequest pickup code verification payload
     * @return verification result
     */
    @PutMapping("/verify")
    public ApiResponse<CodeResponse> verifyPickupCode(@AuthenticationPrincipal Long merchantId,
                                                      @Valid @RequestBody CodeRequest codeRequest) {
        return ApiResponse.success(pickupCodeService.verifyPickupCode(merchantId, codeRequest));
    }
}
