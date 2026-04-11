package com.lastcalleats.order.state;

import com.lastcalleats.order.entity.OrderDO;

/**
 * State object for orders waiting for payment.
 */
public class PendingPaymentState implements OrderState {

    @Override
    public OrderDO.OrderStatus getStatus() {
        return OrderDO.OrderStatus.PENDING_PAYMENT;
    }

    @Override
    public void pay(OrderDO order) {
        order.setStatus(OrderDO.OrderStatus.PAID);
    }

    @Override
    public void cancel(OrderDO order) {
        order.setStatus(OrderDO.OrderStatus.CANCELLED);
    }
}
