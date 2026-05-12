package com.codealpha.hotel_management_system.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class ApiException extends RuntimeException {
    public ApiException(String message) {
        super(message);
    }

    public HttpStatusCode getStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
