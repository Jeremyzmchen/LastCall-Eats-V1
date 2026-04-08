package com.lastcalleats.payment.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PaymentRequest {

    @NotNull
    private Long orderId;

    // 由前端通过 Stripe.js 获取
    @NotNull
    private String paymentMethodId;
}
