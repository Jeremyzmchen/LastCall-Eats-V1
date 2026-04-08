package com.lastcalleats.payment.dto;

import lombok.Data;

// Controller 层解析 Stripe webhook payload 后封装成此对象传给 Service
@Data
public class WebhookRequest {

    // 用于幂等去重
    private String eventId;

    // e.g. payment_intent.succeeded / payment_intent.payment_failed
    private String eventType;

    private String paymentIntentId;

    // 从 PaymentIntent metadata 中提取
    private Long orderId;
}
