package com.codealpha.hotel_management_system.core.controller;

import com.codealpha.hotel_management_system.dto.response   .ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public abstract class BaseController {
    protected ResponseEntity<ApiResponse<?>> ok(ApiResponse<?> response) {
        return ResponseEntity.ok(response);
    }
    protected ResponseEntity<ApiResponse<?>> created(ApiResponse<?> response) {
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    protected ResponseEntity<ApiResponse<?>> noContent(ApiResponse<?> response) {
        return ResponseEntity.ok(response);
    }
    protected ResponseEntity<byte[]> file(byte[] data, String fileName) {
        return ResponseEntity.ok()
                .header("Content-Disposition",
                        "attachment; filename=\"" + fileName + "\"")
                .header("Content-Type", "application/pdf")
                .body(data);
    }
}
