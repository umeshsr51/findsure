package com.findsure.exception;

import org.springframework.http.HttpStatus;

/**
 * Base type for all handled application errors. Carries the machine-readable
 * "code" used in the API's error responses alongside the HTTP status to return.
 */
public class ApiException extends RuntimeException {

    private final String code;
    private final HttpStatus status;

    public ApiException(String code, String message, HttpStatus status) {
        super(message);
        this.code = code;
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
