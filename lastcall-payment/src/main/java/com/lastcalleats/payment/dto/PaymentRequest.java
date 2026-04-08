package com.lastcalleats.payment.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 发起支付请求体。
 * V1 暂时保留结构，payment 模块逻辑未正式开发。
 */
@Data
public class PaymentRequest {

    @NotNull
    private Long orderId;

    // Stripe PaymentMethod ID（前端通过 Stripe.js 获取）
    @NotNull
    private String paymentMethodId;
}
