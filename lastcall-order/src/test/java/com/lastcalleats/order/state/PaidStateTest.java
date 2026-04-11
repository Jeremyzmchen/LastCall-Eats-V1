package com.lastcalleats.order.state;

import com.lastcalleats.common.exception.BusinessException;
import com.lastcalleats.order.entity.OrderDO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for {@link PaidState}.
 */
class PaidStateTest {

    private final PaidState state = new PaidState();

    /**
     * Checks that a paid order can move to COMPLETED.
     */
    @Test
    void complete_shouldMoveOrderToCompleted() {
        OrderDO order = OrderDO.builder().status(OrderDO.OrderStatus.PAID).build();

        state.complete(order);

        assertEquals(OrderDO.OrderStatus.COMPLETED, order.getStatus());
    }

    /**
     * Checks that a paid order cannot be cancelled.
     */
    @Test
    void cancel_shouldThrowBecauseTransitionIsNotAllowed() {
        OrderDO order = OrderDO.builder().status(OrderDO.OrderStatus.PAID).build();

        assertThrows(BusinessException.class, () -> state.cancel(order));
    }
}
