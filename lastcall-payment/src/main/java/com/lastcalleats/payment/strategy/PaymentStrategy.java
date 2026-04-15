package com.lastcalleats.payment.strategy;

import com.lastcalleats.order.entity.OrderDO;
import com.lastcalleats.payment.dto.PaymentRequest;
import com.lastcalleats.payment.dto.PaymentResponse;

/**
 * 支付策略接口，定义统一的支付执行契约。
 * 不同支付平台（Stripe、PayPal 等）各自实现此接口，
 * PaymentService 依赖接口而非具体实现，符合开闭原则。
 */
public interface PaymentStrategy {

    /**
     * 执行支付，返回支付结果。
     *
     * @param order   已校验的订单实体
     * @param request 支付请求，含支付方式 ID 等平台相关参数
     * @return 支付结果
     */
    PaymentResponse pay(OrderDO order, PaymentRequest request);
}
