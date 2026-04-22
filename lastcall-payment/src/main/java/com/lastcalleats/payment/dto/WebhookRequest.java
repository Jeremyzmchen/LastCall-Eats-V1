package com.lastcalleats.payment.dto;

import lombok.Data;

/**
 * Provider-agnostic representation of a webhook event.
 * {@link com.lastcalleats.payment.facade.StripeWebhookFacade} translates the
 * raw Stripe payload into this form so the service layer stays decoupled from
 * the Stripe SDK.
 */
@Data
public class WebhookRequest {

    private String eventId;   // for idempotency
    private String eventType; // e.g. "payment_intent.succeeded"
    private Long orderId;     // extracted from PaymentIntent metadata
}
