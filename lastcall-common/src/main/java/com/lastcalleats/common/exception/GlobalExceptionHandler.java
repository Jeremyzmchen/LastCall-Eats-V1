package com.lastcalleats.common.exception;

import com.lastcalleats.common.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * Centralized exception handler applied across all {@code @RestController} classes.
 * Maps known exception types to structured {@link ApiResponse} bodies so clients
 * receive consistent error payloads regardless of which layer throws.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles business rule violations thrown by the service layer.
     * The HTTP status is derived from the exception's {@link ErrorCode}.
     *
     * @param ex the caught business exception
     * @return a response entity carrying the error code's HTTP status and message
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(BusinessException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ApiResponse.fail(errorCode.getHttpStatus(), ex.getMessage()));
    }

    /**
     * Handles {@code @Valid} failures on request bodies, collecting all field
     * error messages into a single comma-separated string.
     *
     * @param ex the validation exception produced by Spring MVC
     * @return 400 with the concatenated field error messages
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.fail(400, message));
    }

    /**
     * Handles Spring Security authentication failures (missing or invalid token).
     *
     * @param ex the authentication exception
     * @return 401 Unauthorized
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<Void>> handleAuthenticationException(AuthenticationException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.fail(401, "Unauthorized"));
    }

    /**
     * Handles Spring Security access denials for authenticated users who lack
     * the required role or permission.
     *
     * @param ex the access denied exception
     * @return 403 Forbidden
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handleAccessDeniedException(AccessDeniedException ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.fail(403, "Forbidden"));
    }

    /**
     * Catch-all handler for any unrecognized exception. Logs the full stack
     * trace server-side but returns only a generic message to the client to
     * avoid leaking internal details.
     *
     * @param ex the unexpected exception
     * @return 500 Internal Server Error
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception ex) {
        log.error("Unexpected error", ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.fail(500, "Internal server error"));
    }
}
