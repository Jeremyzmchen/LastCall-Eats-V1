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

    /**
     * 支付方式类型，用于 Strategy 路由。
     * 目前支持：
     *   - "STRIPE"    （已实现）
     *   - "APPLE_PAY" （预留扩展）
     *   - "PAYPAL"    （预留扩展）
     */
    @NotNull
    private String paymentType;
}
