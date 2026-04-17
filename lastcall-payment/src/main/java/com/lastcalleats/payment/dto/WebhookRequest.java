package com.lastcalleats.payment.dto;

import lombok.Data;

// Normalized webhook payload passed from controller to service
@Data
public class WebhookRequest {

    private String eventId;   // for idempotency
    private String eventType; // e.g. "payment_intent.succeeded"
    private Long orderId;     // extracted from PaymentIntent metadata
}
