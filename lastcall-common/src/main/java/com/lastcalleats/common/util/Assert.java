package com.lastcalleats.common.util;

import com.lastcalleats.common.exception.BusinessException;
import com.lastcalleats.common.exception.ErrorCode;

/**
 * Lightweight guard utilities for validating preconditions in service methods.
 * Each method throws a {@link BusinessException} with the supplied error code
 * when the condition is not met, keeping validation logic concise at call sites.
 */
public class Assert {

    private Assert() {}

    /**
     * Throws if {@code condition} is {@code false}.
     *
     * @param condition the expression that must be true
     * @param errorCode the error code to raise on failure
     */
    public static void isTrue(boolean condition, ErrorCode errorCode) {
        if (!condition) throw new BusinessException(errorCode);
    }

    /**
     * Throws if {@code obj} is {@code null}.
     *
     * @param obj       the object that must be non-null
     * @param errorCode the error code to raise on failure
     */
    public static void notNull(Object obj, ErrorCode errorCode) {
        if (obj == null) throw new BusinessException(errorCode);
    }

    /**
     * Throws if {@code a} and {@code b} are not equal according to
     * {@link Object#equals}.
     *
     * @param a         the first value
     * @param b         the second value
     * @param errorCode the error code to raise when they differ
     */
    public static void equals(Object a, Object b, ErrorCode errorCode) {
        if (!a.equals(b)) throw new BusinessException(errorCode);
    }

    /**
     * Throws if {@code str} is {@code null} or blank.
     *
     * @param str       the string that must have content
     * @param errorCode the error code to raise on failure
     */
    public static void notBlank(String str, ErrorCode errorCode) {
        if (str == null || str.isBlank()) throw new BusinessException(errorCode);
    }
}
