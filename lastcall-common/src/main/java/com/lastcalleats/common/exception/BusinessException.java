package com.lastcalleats.common.exception;

import lombok.Getter;

/**
 * Application-level exception used to signal known business rule violations.
 * Carries an {@link ErrorCode} so the global handler can map it to the
 * correct HTTP status without inspecting the message string.
 */
@Getter
public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;

    /**
     * Creates an exception whose detail message is taken from the error code.
     *
     * @param errorCode the error code describing the violation
     */
    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    /**
     * Creates an exception with a custom detail message, used when the
     * provider-specific reason (e.g. a Stripe error string) should be surfaced.
     *
     * @param errorCode the error code describing the violation
     * @param message   a more specific description of the failure
     */
    public BusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
