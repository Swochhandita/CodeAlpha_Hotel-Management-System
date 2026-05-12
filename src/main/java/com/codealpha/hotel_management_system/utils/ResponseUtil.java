package com.codealpha.hotel_management_system.utils;
import com.codealpha.hotel_management_system.dto.response.ApiResponse;
import org.springframework.http.HttpStatus;
import java.time.LocalDateTime;

public class ResponseUtil {
    public static ApiResponse<?> getSuccessResponse(String message) {
        return ApiResponse.builder()
                .success(true)
                .message(message)
                .httpStatus(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();
    }
    public static ApiResponse<?> getSuccessResponse(Object data, String message) {
        return ApiResponse.builder()
                .success(true)
                .message(message)
                .data(data)
                .httpStatus(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();
    }
    public static <T> ApiResponse<T> getSuccessResponseWithData(T data, String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage(message);
        response.setData(data);
        response.setHttpStatus(HttpStatus.OK);
        response.setTimestamp(LocalDateTime.now());
        return response;
    }
    public static ApiResponse<?> getCreatedResponse(String message) {
        return ApiResponse.builder()
                .success(true)
                .message(message)
                .httpStatus(HttpStatus.CREATED)
                .timestamp(LocalDateTime.now())
                .build();
    }
    public static ApiResponse<?> getCreatedResponse(Object data, String message) {
        return ApiResponse.builder()
                .success(true)
                .message(message)
                .data(data)
                .httpStatus(HttpStatus.CREATED)
                .timestamp(LocalDateTime.now())
                .build();
    }
    public static <T> ApiResponse<T> getCreatedResponseWithData(T data, String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage(message);
        response.setData(data);
        response.setHttpStatus(HttpStatus.CREATED);
        response.setTimestamp(LocalDateTime.now());
        return response;
    }

    // ── Client errors ─────────────────────────────────────────────────────
    public static ApiResponse<Object> getNotFoundResponse(String message) {
        return ApiResponse.builder()
                .success(false)
                .message(message)
                .httpStatus(HttpStatus.NOT_FOUND)
                .timestamp(LocalDateTime.now())
                .build();
    }
    public static ApiResponse<Object> getBadRequestResponse(String message) {
        return ApiResponse.builder()
                .success(false)
                .message(message)
                .httpStatus(HttpStatus.BAD_REQUEST)
                .timestamp(LocalDateTime.now())
                .build();
    }
    public static ApiResponse<?> getValidationFailureResponse(String message) {
        return ApiResponse.builder()
                .success(false)
                .message(message)
                .httpStatus(HttpStatus.BAD_REQUEST)
                .timestamp(LocalDateTime.now())
                .build();
    }
    public static ApiResponse<Object> getConflictResponse(String message) {
        return ApiResponse.builder()
                .success(false)
                .message(message)
                .httpStatus(HttpStatus.CONFLICT)
                .timestamp(LocalDateTime.now())
                .build();
    }
    public static ApiResponse<Object> getUnauthorizedResponse(String message) {
        return ApiResponse.builder()
                .success(false)
                .message(message)
                .httpStatus(HttpStatus.UNAUTHORIZED)
                .timestamp(LocalDateTime.now())
                .build();
    }
    public static <T> ApiResponse<T> getForbiddenResponse(String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .httpStatus(HttpStatus.FORBIDDEN)
                .timestamp(LocalDateTime.now())
                .build();
    }
    public static ApiResponse<?> getMethodNotAllowedResponse(String message) {
        return ApiResponse.builder()
                .success(false)
                .message(message)
                .httpStatus(HttpStatus.METHOD_NOT_ALLOWED)
                .timestamp(LocalDateTime.now())
                .build();
    }
    // ── Server errors ─────────────────────────────────────────────────────
    public static ApiResponse<?> getInternalServerError(String message) {
        return ApiResponse.builder()
                .success(false)
                .message(message)
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .timestamp(LocalDateTime.now())
                .build();
    }
    public static ApiResponse<?> getTimeoutResponse(String message) {
        return ApiResponse.builder()
                .success(false)
                .message(message)
                .httpStatus(HttpStatus.REQUEST_TIMEOUT)
                .timestamp(LocalDateTime.now())
                .build();
    }
    public static ApiResponse<?> getBeanValidationFailureResponse(String message) {
        return ApiResponse.builder()
                .success(false)
                .message(message)
                .httpStatus(HttpStatus.BAD_REQUEST)
                .timestamp(LocalDateTime.now())
                .build();
    }

}

