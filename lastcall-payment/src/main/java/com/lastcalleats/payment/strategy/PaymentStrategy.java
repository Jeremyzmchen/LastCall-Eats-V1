package com.lastcalleats.payment.strategy;

import com.lastcalleats.order.entity.OrderDO;
import com.lastcalleats.payment.dto.PaymentRequest;
import com.lastcalleats.payment.dto.PaymentResponse;

/**
 * 支付策略接口。每种支付平台实现此接口并声明支持的类型，
 * 新增支付方式只需新增实现类，无需修改现有代码。
 * 扩展示例：ApplePayStrategy("APPLE_PAY")、PaypalStrategy("PAYPAL")
 */
public interface PaymentStrategy {

    /** 是否支持该支付类型，与 PaymentRequest.paymentType 匹配。 */
    boolean supports(String paymentType);

    /** 执行支付，返回支付结果。 */
    PaymentResponse pay(OrderDO order, PaymentRequest request);
}
