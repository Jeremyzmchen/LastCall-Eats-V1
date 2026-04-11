package com.lastcalleats.order.state;

import com.lastcalleats.common.exception.BusinessException;
import com.lastcalleats.order.entity.OrderDO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for terminal order states.
 */
class TerminalStateTest {

    /**
     * Checks that a completed order rejects later changes.
     */
    @Test
    void completedState_shouldRejectFurtherTransitions() {
        CompletedState state = new CompletedState();
        OrderDO order = OrderDO.builder().status(OrderDO.OrderStatus.COMPLETED).build();

        assertEquals(OrderDO.OrderStatus.COMPLETED, state.getStatus());
        assertThrows(BusinessException.class, () -> state.pay(order));
        assertThrows(BusinessException.class, () -> state.cancel(order));
    }

    /**
     * Checks that a cancelled order rejects later changes.
     */
    @Test
    void cancelledState_shouldRejectFurtherTransitions() {
        CancelledState state = new CancelledState();
        OrderDO order = OrderDO.builder().status(OrderDO.OrderStatus.CANCELLED).build();

        assertEquals(OrderDO.OrderStatus.CANCELLED, state.getStatus());
        assertThrows(BusinessException.class, () -> state.pay(order));
        assertThrows(BusinessException.class, () -> state.complete(order));
    }
}
