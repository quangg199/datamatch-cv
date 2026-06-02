package com.example.datamatch.common.presentation.exception;

import com.example.datamatch.common.presentation.dto.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .reduce((a, b) -> a + ", " + b)
                .orElse("Validation error");

        log.warn("Validation failed for {}: {}", request.getRequestURI(), message);

        ApiErrorResponse response = ApiErrorResponse.of(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                message,
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<ApiErrorResponse> handleBadRequest(RuntimeException ex, HttpServletRequest request) {
        log.warn("Bad Request at {}: {}", request.getRequestURI(), ex.getMessage());

        ApiErrorResponse response = ApiErrorResponse.of(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleAllExceptions(Exception ex, HttpServletRequest request) {
        String message = ex.getMessage() != null ? ex.getMessage() : "";

        // Detect AI Provider 429 Quota/Rate Limit
        if (message.contains("429") || message.contains("Quota exceeded") || message.contains("RESOURCE_EXHAUSTED")) {
            log.error("AI Rate Limit / Quota Exceeded at {}: {}", request.getRequestURI(), message);
            ApiErrorResponse response = ApiErrorResponse.of(
                    HttpStatus.TOO_MANY_REQUESTS.value(),
                    HttpStatus.TOO_MANY_REQUESTS.getReasonPhrase(),
                    "AI Provider Rate Limit / Quota Exceeded. Please check your billing or try again later.",
                    request.getRequestURI()
            );
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(response);
        }

        // Generic 500 - Mask internal details from client
        log.error("Unhandled Internal Error at {}: ", request.getRequestURI(), ex);
        ApiErrorResponse response = ApiErrorResponse.of(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                "An unexpected internal server error occurred.",
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
