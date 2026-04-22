package com.lastcalleats.payment.dto;

import lombok.Builder;
import lombok.Getter;

/**
 * Response returned after a payment intent is created or confirmed.
 * When {@code requiresAction} is {@code true} the client must complete an
 * additional step (such as 3D Secure) using the provided {@code clientSecret}.
 */
@Getter
@Builder
public class PaymentResponse {

    private Long orderId;
    private String status;
    private String paymentIntentId;

    // True when the client must handle an extra step (e.g. 3D Secure)
    private Boolean requiresAction;
    private String clientSecret;
}
