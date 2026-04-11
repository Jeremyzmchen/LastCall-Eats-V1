package com.lastcalleats.order.factory;

import com.lastcalleats.common.util.PickupCodeUtil;
import com.lastcalleats.order.entity.OrderDO;
import org.springframework.stereotype.Component;

/**
 * Generates six-digit pickup codes.
 */
@Component
public class NumericCodeGenerator implements PickupCodeGenerator {

    @Override
    public String type() {
        return "NUMERIC";
    }

    @Override
    public String generate(OrderDO order) {
        return PickupCodeUtil.generateNumericCode();
    }
}
