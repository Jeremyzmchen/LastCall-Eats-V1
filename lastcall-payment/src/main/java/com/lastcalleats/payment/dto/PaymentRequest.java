package com.lastcalleats.payment.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PaymentRequest {

    @NotNull
    private Long orderId;

    // Payment method ID passed from Stripe → frontend → backend
    @NotNull
    private String paymentMethodId;

    // Routes to the matching PaymentStrategy; e.g. "STRIPE", "APPLE_PAY", "PAYPAL"
    @NotNull
    private String paymentType;
}
