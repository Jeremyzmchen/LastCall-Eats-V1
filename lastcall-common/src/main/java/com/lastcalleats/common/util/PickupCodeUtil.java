package com.lastcalleats.common.util;

import java.security.SecureRandom;

/**
 * Utility for generating pickup codes handed to customers after payment.
 * Uses {@link SecureRandom} rather than {@link java.util.Random} to avoid
 * predictable sequences that could allow code guessing at the counter.
 */
public class PickupCodeUtil {

    private static final SecureRandom RANDOM = new SecureRandom();

    private PickupCodeUtil() {}

    /**
     * Generates a random 6-digit numeric code in the range [100000, 999999].
     *
     * @return a 6-character string of decimal digits
     */
    public static String generateNumericCode() {
        int code = 100000 + RANDOM.nextInt(900000);
        return String.valueOf(code);
    }
}
