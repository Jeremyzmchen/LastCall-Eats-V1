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
import com.lastcalleats.payment.strategy.PaymentStrategy;
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
  private final PaymentStrategy paymentStrategy;

  @Override
  @Transactional
  public PaymentResponse createPaymentIntent(Long userId, PaymentRequest request) {
    OrderDO order = orderRepo.findById(request.getOrderId())
        .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));
    Assert.equals(order.getUserId(), userId, ErrorCode.BAD_REQUEST);
    Assert.isTrue(order.getStatus() == OrderStatus.PENDING_PAYMENT, ErrorCode.ORDER_STATUS_INVALID);

    PaymentResponse response = paymentStrategy.pay(order, request);
    markOrderPaid(order);
    return response;
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
