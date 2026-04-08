package com.lastcalleats.payment.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PaymentResponse {

    private Long orderId;
    private String status;
    private String paymentIntentId;

    // 为 true 时前端需额外处理（如 3D Secure 验证）
    private Boolean requiresAction;
    private String clientSecret;
}
