package com.tarikh.interview.starling.integration.exceptions;

import org.springframework.http.HttpStatus;

public class ErrorDTO {
    private final String message;
    private final HttpStatus httpStatus;

    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public ErrorDTO(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
