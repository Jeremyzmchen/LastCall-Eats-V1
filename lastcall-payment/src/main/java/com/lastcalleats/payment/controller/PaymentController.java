package com.lastcalleats.payment.controller;

import com.lastcalleats.common.response.ApiResponse;
import com.lastcalleats.payment.config.StripeConfig;
import com.lastcalleats.payment.dto.PaymentRequest;
import com.lastcalleats.payment.dto.PaymentResponse;
import com.lastcalleats.payment.dto.WebhookRequest;
import com.lastcalleats.payment.service.PaymentService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeObject;
import com.stripe.net.Webhook;
import jakarta.validation.Valid;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {
  private final PaymentService paymentService;
  private final StripeConfig stripeConfig;

  @PostMapping("/create")
  public ApiResponse<PaymentResponse> createPaymentIntent(
      // TODO: auth 模块需要封装一层 LoginUser 替代 暴力获取 UserDetails
      @AuthenticationPrincipal UserDetails loginUser,
      @Valid @RequestBody PaymentRequest request
  ) {
    //Long userId = loginUser.getUserId();
    Long userId = Long.parseLong(loginUser.getUsername());
    return ApiResponse.success(paymentService.createPaymentIntent(userId, request));
  }


  // 兜底机制，Stripe异步发送hook，防止网络抖动
  // 本地通过Stripe CLI监听转发
  @PostMapping("/webhook")
  public ResponseEntity<Void> webhook(
      // 接收Stripe签名，验证真实性
      @RequestHeader("Stripe-Signature") String sigHeader,
      @RequestBody String payload
  ) {
    Event event;

    try {
      // 通过 Stripe SDK 验证签名(hook信息真假）
      event = Webhook.constructEvent(payload, sigHeader, stripeConfig.getWebhookSecret());
    } catch (SignatureVerificationException e) {
      log.warn("Invalid Stripe webhook signature");
      return ResponseEntity.badRequest().build();
    }

    Optional<StripeObject> stripeObjectOptional = event.getDataObjectDeserializer().getObject();
    if (stripeObjectOptional.isEmpty()) {
      log.warn("Failed to deserialize Stripe event: {}", event.getType());
      return ResponseEntity.ok().build();
    }
    if (stripeObjectOptional.get() instanceof PaymentIntent intent) {
      String orderIdStr = intent.getMetadata().get("orderId");
      if (orderIdStr == null) {
        log.warn("Payment intent lost orderId metadata: {}", intent.getId());
        return ResponseEntity.ok().build();
      }

      WebhookRequest webhookRequest = new WebhookRequest();
      webhookRequest.setEventId(event.getId());
      webhookRequest.setEventType(event.getType());
      webhookRequest.setOrderId(Long.parseLong(orderIdStr));
      paymentService.handleWebhook(webhookRequest);
    }

    return ResponseEntity.ok().build();
  }
}
