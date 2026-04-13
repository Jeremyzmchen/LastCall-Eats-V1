package com.lastcalleats.order.service;

import com.lastcalleats.order.dto.CodeRequest;
import com.lastcalleats.order.dto.CodeResponse;

// In V1, OrderService handles pickup code creation.
// In V2, this service can handle expiration, resend, and batch verification.
/**
 * Service interface for pickup code verification.
 */
public interface PickupCodeService {

    /**
     * Verifies a pickup code.
     *
     * @param merchantId current merchant ID
     * @param request request with a pickup code or QR string
     * @return verification result
     */
    CodeResponse verifyPickupCode(Long merchantId, CodeRequest request);
}
