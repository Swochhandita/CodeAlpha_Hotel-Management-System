package com.codealpha.hotel_management_system.exception.handler;

import com.codealpha.hotel_management_system.dto.response.ApiResponse;
import com.codealpha.hotel_management_system.exception.*;
import com.codealpha.hotel_management_system.utils.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleResourceNotFoundException(
            ResourceNotFoundException ex) {
        ApiResponse<Object> apiResponse = ResponseUtil.getNotFoundResponse(ex.getMessage());
        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse<Object>> handleApiException(ApiException ex) {
        ApiResponse<Object> apiResponse = ResponseUtil.getBadRequestResponse(ex.getMessage());
        return new ResponseEntity<>(apiResponse, ex.getStatus());
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiResponse<Object>> handleDuplicateResourceException(
            DuplicateResourceException ex) {
        ApiResponse<Object> apiResponse = ResponseUtil.getConflictResponse(ex.getMessage());
        return new ResponseEntity<>(apiResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(RoomNotAvailableException.class)
    public ResponseEntity<ApiResponse<Object>> handleRoomNotAvailableException(
            RoomNotAvailableException ex) {
        ApiResponse<Object> apiResponse = ResponseUtil.getConflictResponse(ex.getMessage());
        return new ResponseEntity<>(apiResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiResponse<Object>> handleUnauthorizedException(
            UnauthorizedException ex) {
        ApiResponse<Object> apiResponse = ResponseUtil.getUnauthorizedResponse(ex.getMessage());
        return new ResponseEntity<>(apiResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<Object>> handleBadCredentialsException(
            BadCredentialsException ex) {
        ApiResponse<Object> apiResponse = ResponseUtil.getUnauthorizedResponse(
                "Invalid email or password");
        return new ResponseEntity<>(apiResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Object>> handleAccessDeniedException(
            AccessDeniedException ex) {
        ApiResponse<Object> apiResponse = ResponseUtil.getForbiddenResponse(
                "You do not have permission to perform this action");
        return new ResponseEntity<>(apiResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleException(Exception ex) {
        log.error("Unexpected error occurred", ex);
        ApiResponse<?> apiResponse = ResponseUtil.getInternalServerError("An unexpected error occurred. Please try again later");
        return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
