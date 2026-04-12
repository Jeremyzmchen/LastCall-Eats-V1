package com.lastcalleats.payment.service.impl;

import com.lastcalleats.common.exception.BusinessException;
import com.lastcalleats.common.exception.ErrorCode;
import com.lastcalleats.common.util.Assert;
import com.lastcalleats.common.util.PickupCodeUtil;
import com.lastcalleats.order.entity.OrderDO;
import com.lastcalleats.order.entity.OrderDO.OrderStatus;
import com.lastcalleats.order.entity.PickupCodeDO;
import com.lastcalleats.order.repository.OrderRepo;
import com.lastcalleats.order.repository.PickupCodeRepo;
import com.lastcalleats.payment.dto.PaymentRequest;
import com.lastcalleats.payment.dto.PaymentResponse;
import com.lastcalleats.payment.dto.WebhookRequest;
import com.lastcalleats.payment.service.PaymentService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
  private final OrderRepo orderRepo;
  private final PickupCodeRepo pickupCodeRepo;

  @Override
  @Transactional
  public PaymentResponse createPaymentIntent(Long userId, PaymentRequest request) {
    // 校验订单
    OrderDO order = orderRepo.findById(request.getOrderId())
        .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));
    Assert.equals(order.getUserId(), userId, ErrorCode.BAD_REQUEST);
    Assert.isTrue(order.getStatus() == OrderStatus.PENDING_PAYMENT, ErrorCode.ORDER_STATUS_INVALID);

    // 创建Intent
    try {
      // build API params，confirm=true 直接在创建时确认支付
      PaymentIntentCreateParams createParams = PaymentIntentCreateParams.builder()
          // Stripe API 需要转化单位为分
          .setAmount(order.getPrice().multiply(java.math.BigDecimal.valueOf(100)).longValue())
          .setCurrency("usd")
          .setPaymentMethod(request.getPaymentMethodId())
          .setConfirm(true)
          .setAutomaticPaymentMethods(
              PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                  .setEnabled(true)
                  .setAllowRedirects(PaymentIntentCreateParams.AutomaticPaymentMethods.AllowRedirects.NEVER)
                  .build()
          )
          .putMetadata("userId", String.valueOf(userId))
          .putMetadata("orderId", String.valueOf(order.getId()))
          .build();

      PaymentIntent intent = PaymentIntent.create(createParams);
      Assert.equals("succeeded", intent.getStatus(), ErrorCode.PAYMENT_FAILED);

      // 更新订单数据库信息
      markOrderPaid(order);

      return PaymentResponse.builder()
          .orderId(order.getId())
          .status("succeeded")
          .paymentIntentId(intent.getId())
          .build();
    } catch (StripeException e) {
      log.error("Stripe payment failed, userId={}, orderId={}", userId, request.getOrderId());
      throw new BusinessException(ErrorCode.PAYMENT_FAILED, e.getMessage());
    }
  }


  @Override
  @Transactional
  public void handleWebhook(WebhookRequest request) {
    switch (request.getEventType()) {
      case "payment_intent.succeeded" -> {
        OrderDO order = orderRepo.findById(request.getOrderId())
            .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));
        if (order.getStatus() == OrderStatus.PENDING_PAYMENT) {
          markOrderPaid(order);
        }
      }
      case "payment_intent.payment_failed" -> {
        OrderDO order = orderRepo.findById(request.getOrderId()).orElse(null);
        if (order != null && order.getStatus() == OrderStatus.PENDING_PAYMENT) {
          order.setStatus(OrderStatus.CANCELLED);
          orderRepo.save(order);
        }
      }
      default -> log.debug("Unhandled webhook event: {}", request.getEventType());
    }
  }

  private void markOrderPaid(OrderDO order) {
    order.setStatus(OrderStatus.PAID);
    orderRepo.save(order);
    if (pickupCodeRepo.findByOrderId(order.getId()).isEmpty()) {
      pickupCodeRepo.save(PickupCodeDO.builder()
          .orderId(order.getId())
          .numericCode(PickupCodeUtil.generateNumericCode())
          .used(false)
          .build());
    }
  }
}
