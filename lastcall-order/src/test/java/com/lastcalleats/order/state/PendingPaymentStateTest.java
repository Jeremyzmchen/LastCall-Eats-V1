package com.lastcalleats.order.state;

import com.lastcalleats.order.entity.OrderDO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for {@link PendingPaymentState}.
 */
class PendingPaymentStateTest {

    private final PendingPaymentState state = new PendingPaymentState();

    /**
     * Checks that a pending order can move to PAID.
     */
    @Test
    void pay_shouldMoveOrderToPaid() {
        OrderDO order = OrderDO.builder().status(OrderDO.OrderStatus.PENDING_PAYMENT).build();

        state.pay(order);

        assertEquals(OrderDO.OrderStatus.PAID, order.getStatus());
    }

    /**
     * Checks that a pending order can move to CANCELLED.
     */
    @Test
    void cancel_shouldMoveOrderToCancelled() {
        OrderDO order = OrderDO.builder().status(OrderDO.OrderStatus.PENDING_PAYMENT).build();

        state.cancel(order);

        assertEquals(OrderDO.OrderStatus.CANCELLED, order.getStatus());
    }
}
