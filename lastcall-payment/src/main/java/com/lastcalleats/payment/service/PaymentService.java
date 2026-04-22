package com.lastcalleats.payment.service;

import com.lastcalleats.payment.dto.PaymentRequest;
import com.lastcalleats.payment.dto.PaymentResponse;
import com.lastcalleats.payment.dto.WebhookRequest;

/**
 * Defines the contract for payment processing in the LastCall Eats platform.
 * Implementations are responsible for creating payment intents and reacting to
 * asynchronous payment events forwarded from the payment provider's webhook.
 */
public interface PaymentService {

  /**
   * Creates a payment intent for the given order and immediately confirms it
   * using the payment method supplied in the request.
   *
   * @param userId  the authenticated user initiating the payment
   * @param request holds the order ID, payment method token, and payment type
   * @return a {@link PaymentResponse} carrying the intent ID and final status
   * @throws com.lastcalleats.common.exception.BusinessException if the order is
   *         not found, belongs to a different user, or is not in PENDING_PAYMENT state
   */
  PaymentResponse createPaymentIntent(Long userId, PaymentRequest request);

  /**
   * Processes an inbound webhook event after signature verification.
   * Currently handles {@code payment_intent.succeeded} and
   * {@code payment_intent.payment_failed}; unknown event types are silently ignored.
   *
   * @param request a parsed, provider-agnostic representation of the webhook event
   */
  void handleWebhook(WebhookRequest request);
}
