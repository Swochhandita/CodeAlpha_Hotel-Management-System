package com.codealpha.hotel_management_system.mapper;

import com.codealpha.hotel_management_system.dto.requests.RegisterRequest;
import com.codealpha.hotel_management_system.dto.response.AuthResponse;
import com.codealpha.hotel_management_system.dto.response.UserResponse;
import com.codealpha.hotel_management_system.entity.User;
import com.codealpha.hotel_management_system.enums.Role;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User toEntity(RegisterRequest request, String encodedPassword) {
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(encodedPassword);//the password is encoded before saving in the database
        user.setPhone(request.getPhone());
        user.setRole(Role.GUEST);//Every new user start as the guest by default
        return user;
    }

    public AuthResponse toAuthResponse(User user, String token) {
        AuthResponse response = new AuthResponse();
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole().name());
        // token passed in directly and not derived from entity as not saved in the database
        response.setToken(token);
        response.setTokenType("Bearer");
        return response;
    }

    public UserResponse toUserResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhone());
        response.setRole(user.getRole());
        response.setCreatedAt(user.getCreatedAt());
        return response;
    }
}
