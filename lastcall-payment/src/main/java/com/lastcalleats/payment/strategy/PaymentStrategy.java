package com.lastcalleats.payment.strategy;

import com.lastcalleats.order.entity.OrderDO;
import com.lastcalleats.payment.dto.PaymentRequest;
import com.lastcalleats.payment.dto.PaymentResponse;

/** Add a new payment provider by implementing this interface — no existing code changes required. */
public interface PaymentStrategy {

    /** Returns true if this strategy handles the given paymentType. */
    boolean supports(String paymentType);

    PaymentResponse pay(OrderDO order, PaymentRequest request);
}
