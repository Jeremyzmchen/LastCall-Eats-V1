package com.lastcalleats.payment.service;

import com.lastcalleats.payment.dto.PaymentRequest;
import com.lastcalleats.payment.dto.PaymentResponse;
import com.lastcalleats.payment.dto.WebhookRequest;

public interface PaymentService {
  PaymentResponse createPaymentIntent(Long userId, PaymentRequest request);
  void handleWebhook(WebhookRequest request);
}
