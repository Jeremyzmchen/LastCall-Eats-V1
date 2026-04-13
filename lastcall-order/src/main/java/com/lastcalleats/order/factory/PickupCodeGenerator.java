package com.lastcalleats.order.factory;

import com.lastcalleats.order.entity.OrderDO;

/**
 * Interface for pickup code generators.
 */
public interface PickupCodeGenerator {

    /**
     * Gets the type key for this generator.
     *
     * @return generator type
     */
    String type();

    /**
     * Generates a pickup code for the given order.
     *
     * @param order target order
     * @return generated pickup code
     */
    String generate(OrderDO order);
}
