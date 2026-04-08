package com.lastcalleats.payment.dto;

import lombok.Data;

/**
 * Stripe Webhook 回调请求体。
 *
 * <p>Stripe 会向我们的 webhook 端点 POST 一个 Event JSON。
 * Controller 层直接读取原始 payload（String）+ Stripe-Signature header 进行签名验证，
 * 本 DTO 不作为 @RequestBody 使用，而是封装验证后的关键字段供 Service 层处理。</p>
 */
@Data
public class WebhookRequest {

    // Stripe Event ID（用于幂等去重）
    private String eventId;

    // 事件类型，如 payment_intent.succeeded / payment_intent.payment_failed
    private String eventType;

    // 关联的 PaymentIntent ID
    private String paymentIntentId;

    // 关联的内部订单 ID（从 PaymentIntent metadata 中提取）
    private Long orderId;
}
