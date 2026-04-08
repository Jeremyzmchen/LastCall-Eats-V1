package com.lastcalleats.payment.dto;

import lombok.Builder;
import lombok.Getter;

/**
 * 支付结果响应体。
 * V1 暂时保留结构，payment 模块逻辑未正式开发。
 */
@Getter
@Builder
public class PaymentResponse {

    private Long orderId;
    private String status;

    // Stripe 返回的 PaymentIntent ID，用于前端确认
    private String paymentIntentId;

    // 是否需要前端额外操作（如 3D Secure）
    private Boolean requiresAction;
    private String clientSecret;
}
