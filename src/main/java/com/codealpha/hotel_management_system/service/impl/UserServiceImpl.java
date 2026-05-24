package com.codealpha.hotel_management_system.service.impl;

import com.codealpha.hotel_management_system.dto.response.ApiResponse;
import com.codealpha.hotel_management_system.dto.response.PagedResponse;
import com.codealpha.hotel_management_system.dto.response.UserResponse;
import com.codealpha.hotel_management_system.entity.User;
import com.codealpha.hotel_management_system.enums.Role;
import com.codealpha.hotel_management_system.exception.ApiException;
import com.codealpha.hotel_management_system.exception.ResourceNotFoundException;
import com.codealpha.hotel_management_system.mapper.UserMapper;
import com.codealpha.hotel_management_system.repository.UserRepository;
import com.codealpha.hotel_management_system.service.UserService;
import com.codealpha.hotel_management_system.utils.ResponseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<?> getUserById(Integer id) {
        log.debug("Fetching user with id: {}", id);
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        UserResponse response = userMapper.toUserResponse(user);
        // ResponseUtil handles building the consistent response structure
        return ResponseUtil.getSuccessResponseWithData(response, "User fetched successfully");
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<?> list(Pageable pageable) {
        Page<User> userPage = userRepository.findAll(pageable);
        PagedResponse<UserResponse> response = PagedResponse.fromPage(userPage, userMapper::toUserResponse);
        return ResponseUtil.getSuccessResponseWithData(response, "Users fetched successfully");
    }

    @Override
    @Transactional
    public ApiResponse<?> deleteUser(Integer id) {
        log.debug("Deleting user with id: {}", id);
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        // Because reservations have cascade settings,
        // related reservations will also be handled
        userRepository.delete(user);
        log.info("User with id: {} deleted successfully", id);
        return ResponseUtil.getSuccessResponse("User deleted successfully");
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<?> getMyProfile(String email) {
        log.debug("Fetching profile for email: {}", email);
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        UserResponse response = userMapper.toUserResponse(user);
        return ResponseUtil.getSuccessResponseWithData(response, "Profile fetched successfully");
    }

    @Override
    @Transactional
    public ApiResponse<?> promoteToAdmin(Integer id) {
        log.debug("Promoting user with id: {} to admin", id);
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        if (user.getRole() == Role.ADMIN) {
            throw new ApiException("User is already an admin", HttpStatus.BAD_REQUEST);
        }
        user.setRole(Role.ADMIN);
        userRepository.save(user);
        log.info("User with id: {} promoted to admin successfully", id);
        return ResponseUtil.getSuccessResponseWithData(userMapper.toUserResponse(user), "User promoted to admin successfully");
    }
}
