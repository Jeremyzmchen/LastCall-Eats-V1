package com.lastcalleats.order.controller;

import com.lastcalleats.common.response.ApiResponse;
import com.lastcalleats.common.security.IdentityResolver;
import com.lastcalleats.order.dto.CodeRequest;
import com.lastcalleats.order.dto.CodeResponse;
import com.lastcalleats.order.service.PickupCodeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
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
    private final IdentityResolver identityResolver;

    public PickupController(PickupCodeService pickupCodeService, IdentityResolver identityResolver) {
        this.pickupCodeService = pickupCodeService;
        this.identityResolver = identityResolver;
    }

    /**
     * Verifies a pickup code from a merchant.
     *
     * @param httpRequest current request
     * @param codeRequest pickup code verification payload
     * @return verification result
     */
    @PutMapping("/verify")
    public ApiResponse<CodeResponse> verifyPickupCode(HttpServletRequest httpRequest,
                                                      @Valid @RequestBody CodeRequest codeRequest) {
        Long merchantId = identityResolver.resolveCurrentMerchantId(httpRequest);
        return ApiResponse.success(pickupCodeService.verifyPickupCode(merchantId, codeRequest));
    }
}
