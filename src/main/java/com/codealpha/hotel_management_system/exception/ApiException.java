package com.codealpha.hotel_management_system.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
@Getter
public class ApiException extends RuntimeException {
    private final HttpStatus status;
    public ApiException(String message) {
        super(message);
        this.status = HttpStatus.BAD_REQUEST;
    }
    public ApiException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
