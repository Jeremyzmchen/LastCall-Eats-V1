package com.lastcalleats.payment.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PaymentRequest {

    @NotNull
    private Long orderId;

    // Stripe -> frontend -> backend
    @NotNull
    private String paymentMethodId;
}
