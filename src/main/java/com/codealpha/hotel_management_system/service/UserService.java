package com.codealpha.hotel_management_system.service;

import com.codealpha.hotel_management_system.dto.response.ApiResponse;
import org.springframework.data.domain.Pageable;

public interface UserService {
    ApiResponse<?> getUserById(Integer id);
    // Used by: admin to see all registered users
    ApiResponse<?> list(Pageable pageable);
    ApiResponse<?> deleteUser(Integer id);
    // Used by: guest to view their own profile
    // email comes from JWT token — extracted in the controller
    ApiResponse<?> getMyProfile(String email);
    ApiResponse<?> promoteToAdmin(Integer id);
}
