package com.lastcalleats.order.state;

import com.lastcalleats.order.entity.OrderDO;

/**
 * State object for paid orders.
 */
public class PaidState implements OrderState {

    @Override
    public OrderDO.OrderStatus getStatus() {
        return OrderDO.OrderStatus.PAID;
    }

    @Override
    public void complete(OrderDO order) {
        order.setStatus(OrderDO.OrderStatus.COMPLETED);
    }
}
