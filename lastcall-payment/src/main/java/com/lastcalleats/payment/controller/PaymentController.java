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

@Slf4j
@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {
  private final PaymentService paymentService;
  private final StripeWebhookFacade stripeWebhookFacade;

  @PostMapping("/create")
  public ApiResponse<PaymentResponse> createPaymentIntent(
      @AuthenticationPrincipal Long userId,
      @Valid @RequestBody PaymentRequest request
  ) {
    return ApiResponse.success(paymentService.createPaymentIntent(userId, request));
  }

  // 兜底机制，Stripe 异步发送 webhook，防止网络抖动；本地通过 Stripe CLI 监听转发
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
