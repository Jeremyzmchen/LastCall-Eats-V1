package com.lastcalleats.order.state;

import com.lastcalleats.order.entity.OrderDO;

/**
 * State object for cancelled orders.
 */
public class CancelledState implements OrderState {

    @Override
    public OrderDO.OrderStatus getStatus() {
        return OrderDO.OrderStatus.CANCELLED;
    }
}
