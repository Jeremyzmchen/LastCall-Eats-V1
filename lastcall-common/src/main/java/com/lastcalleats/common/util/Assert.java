package com.lastcalleats.common.util;

import com.lastcalleats.common.exception.BusinessException;
import com.lastcalleats.common.exception.ErrorCode;

public class Assert {

    private Assert() {}

    // condition 为 false 时抛异常
    public static void isTrue(boolean condition, ErrorCode errorCode) {
        if (!condition) {
            throw new BusinessException(errorCode);
        }
    }

    // obj 为 null 时抛异常
    public static void notNull(Object obj, ErrorCode errorCode) {
        if (obj == null) {
            throw new BusinessException(errorCode);
        }
    }

    // 两个值不相等时抛异常
    public static void equals(Object a, Object b, ErrorCode errorCode) {
        if (!a.equals(b)) {
            throw new BusinessException(errorCode);
        }
    }

    // 字符串为空时抛异常
    public static void notBlank(String str, ErrorCode errorCode) {
        if (str == null || str.isBlank()) {
            throw new BusinessException(errorCode);
        }
    }
}
