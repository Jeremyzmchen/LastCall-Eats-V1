package com.lastcalleats.payment.controller;

import com.lastcalleats.common.response.ApiResponse;
import com.lastcalleats.payment.dto.PaymentRequest;
import com.lastcalleats.payment.dto.PaymentResponse;
import com.lastcalleats.payment.dto.WebhookRequest;
import com.lastcalleats.payment.facade.StripeWebhookFacade;
import com.lastcalleats.payment.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * REST endpoints for payment operations.
 * The {@code /create} endpoint is called by the mobile client after the user
 * confirms checkout; the {@code /webhook} endpoint receives asynchronous
 * payment events forwarded from Stripe CLI during development.
 */
@Slf4j
@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {
  private final PaymentService paymentService;
  private final StripeWebhookFacade stripeWebhookFacade;

  /**
   * Initiates and immediately confirms a payment intent for the authenticated user.
   *
   * @param userId  injected from the JWT principal
   * @param request the order and payment method details
   * @return a wrapped {@link PaymentResponse} with the intent status
   */
  @PostMapping("/create")
  public ApiResponse<PaymentResponse> createPaymentIntent(
      @AuthenticationPrincipal Long userId,
      @Valid @RequestBody PaymentRequest request
  ) {
    return ApiResponse.success(paymentService.createPaymentIntent(userId, request));
  }

  /**
   * Receives a raw Stripe webhook, verifies the signature via {@link StripeWebhookFacade},
   * and delegates to the service layer. Returns 400 if the signature is invalid or the
   * payload cannot be parsed.
   *
   * @param sigHeader the {@code Stripe-Signature} header sent by Stripe
   * @param payload   the raw JSON body (must not be pre-parsed)
   * @return 200 on success, 400 on bad signature or unrecognized payload
   */
  // Fallback for async Stripe events; forward locally via Stripe CLI
  @PostMapping("/webhook")
  public ResponseEntity<Void> webhook(
      @RequestHeader("Stripe-Signature") String sigHeader,
      @RequestBody String payload
  ) {
    Optional<WebhookRequest> webhookRequest = stripeWebhookFacade.parse(payload, sigHeader);
    if (webhookRequest.isEmpty()) {
      return ResponseEntity.badRequest().build();
    }
    paymentService.handleWebhook(webhookRequest.get());
    return ResponseEntity.ok().build();
  }
}
