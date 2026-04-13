package com.lastcalleats.order.factory;

import com.lastcalleats.order.entity.OrderDO;
import org.springframework.stereotype.Component;

/**
 * Generates a short QR string for the frontend.
 */
@Component
public class QRCodeGenerator implements PickupCodeGenerator {

    @Override
    public String type() {
        return "QR";
    }

    @Override
    public String generate(OrderDO order) {
        return "ORDER:" + order.getId();
    }
}
