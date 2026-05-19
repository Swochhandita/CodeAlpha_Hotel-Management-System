package com.codealpha.hotel_management_system.service;

import com.codealpha.hotel_management_system.dto.requests.LoginRequest;
import com.codealpha.hotel_management_system.dto.requests.RegisterRequest;
import com.codealpha.hotel_management_system.dto.response.ApiResponse;

public interface AuthService {
    ApiResponse<?> register(RegisterRequest request);
    // Returns JWT token on success
    ApiResponse<?> login(LoginRequest request);
}
