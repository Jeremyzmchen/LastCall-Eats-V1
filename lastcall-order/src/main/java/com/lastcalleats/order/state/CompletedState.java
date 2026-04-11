package com.lastcalleats.order.state;

import com.lastcalleats.order.entity.OrderDO;

/**
 * State object for completed orders.
 */
public class CompletedState implements OrderState {

    @Override
    public OrderDO.OrderStatus getStatus() {
        return OrderDO.OrderStatus.COMPLETED;
    }
}
