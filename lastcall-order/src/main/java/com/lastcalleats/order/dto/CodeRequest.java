package com.lastcalleats.order.dto;

import lombok.Data;

/**
 * Request body for pickup code verification.
 */
@Data
public class CodeRequest {

    /**
     * Optional six-digit code for manual verification.
     */
    private String pickupCode;

    /**
     * Optional QR string for scan verification.
     */
    private String qrCodeContent;
}
