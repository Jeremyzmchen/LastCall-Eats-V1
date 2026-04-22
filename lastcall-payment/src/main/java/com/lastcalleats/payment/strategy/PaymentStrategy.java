package com.lastcalleats.payment.strategy;

import com.lastcalleats.order.entity.OrderDO;
import com.lastcalleats.payment.dto.PaymentRequest;
import com.lastcalleats.payment.dto.PaymentResponse;

/**
 * Strategy interface for payment provider integrations.
 * Adding a new provider (e.g. Apple Pay) only requires a new implementation;
 * no changes to the service layer are needed.
 */
public interface PaymentStrategy {

    /**
     * Returns {@code true} if this strategy can handle the given payment type string.
     *
     * @param paymentType a case-insensitive token such as {@code "STRIPE"} or {@code "APPLE_PAY"}
     * @return {@code true} when this strategy owns that payment type
     */
    boolean supports(String paymentType);

    /**
     * Executes the payment against the provider and returns the result.
     *
     * @param order   the order being paid; used to derive the charge amount
     * @param request contains the provider-specific payment method token
     * @return a {@link PaymentResponse} with the provider's intent ID and status
     * @throws com.lastcalleats.common.exception.BusinessException if the provider
     *         rejects the payment or an SDK-level error occurs
     */
    PaymentResponse pay(OrderDO order, PaymentRequest request);
}

