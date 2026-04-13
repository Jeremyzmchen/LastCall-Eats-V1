package com.lastcalleats.order.factory;

import com.lastcalleats.order.entity.OrderDO;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for {@link PickupCodeFactory}.
 */
class PickupCodeFactoryTest {

    private final PickupCodeFactory factory = new PickupCodeFactory(List.of(
            new NumericCodeGenerator(),
            new QRCodeGenerator()
    ));

    /**
     * Checks that the factory returns a numeric code.
     */
    @Test
    void generate_shouldReturnNumericCodeForNumericType() {
        String result = factory.generate(buildOrder(), "NUMERIC");

        assertEquals(6, result.length());
        assertTrue(result.chars().allMatch(Character::isDigit));
    }

    /**
     * Checks that the factory returns a QR string.
     */
    @Test
    void generate_shouldReturnQrPayloadForQrType() {
        String result = factory.generate(buildOrder(), "QR");

        assertEquals("ORDER:10", result);
    }

    /**
     * Checks that the factory rejects an unknown type.
     */
    @Test
    void generate_shouldRejectUnknownType() {
        assertThrows(RuntimeException.class, () -> factory.generate(buildOrder(), "UNKNOWN"));
    }

    /**
     * Creates a simple paid order for the tests.
     *
     * @return test order
     */
    private OrderDO buildOrder() {
        return OrderDO.builder()
                .id(10L)
                .userId(20L)
                .merchantId(30L)
                .listingId(40L)
                .status(OrderDO.OrderStatus.PAID)
                .build();
    }
}
