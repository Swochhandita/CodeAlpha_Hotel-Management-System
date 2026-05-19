package com.codealpha.hotel_management_system.service.impl;

import com.codealpha.hotel_management_system.config.jwt.CustomUserDetails;
import com.codealpha.hotel_management_system.config.jwt.JwtUtils;
import com.codealpha.hotel_management_system.dto.requests.LoginRequest;
import com.codealpha.hotel_management_system.dto.requests.RegisterRequest;
import com.codealpha.hotel_management_system.dto.response.ApiResponse;
import com.codealpha.hotel_management_system.dto.response.AuthResponse;
import com.codealpha.hotel_management_system.entity.User;
import com.codealpha.hotel_management_system.exception.ApiException;
import com.codealpha.hotel_management_system.exception.DuplicateResourceException;
import com.codealpha.hotel_management_system.mapper.UserMapper;
import com.codealpha.hotel_management_system.repository.UserRepository;
import com.codealpha.hotel_management_system.service.AuthService;
import com.codealpha.hotel_management_system.utils.ResponseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    @Override
    public ApiResponse<?> register(RegisterRequest request) {
        log.debug("Registering new user with email: {}", request.getEmail());
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already registered: " + request.getEmail());
        }
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        // Convert RegisterRequest → User entity via mapper
        // encodedPassword is passed separately — mapper sets it on the entity
        User user = userMapper.toEntity(request, encodedPassword);
        // Save user to DB
        // JPA sets id, createdAt, updatedAt automatically
        User saved = userRepository.save(user);
        // Generate JWT token for the newly registered user
        // We wrap the user in CustomUserDetails because JwtUtils
        // needs a UserDetails object — not a raw User entity
        CustomUserDetails userDetails = new CustomUserDetails(saved);
        String token = jwtUtils.generateToken(userDetails);//once the user is registered we allow them directly and in login we have used generate token for the returning users.
        // Build the response using mapper
        // toAuthResponse maps user fields + sets the token
        AuthResponse authResponse = userMapper.toAuthResponse(saved, token);
        log.info("User registered successfully with email: {}", saved.getEmail());
        return ResponseUtil.getCreatedResponseWithData(authResponse, "Registration successful. Welcome to StayEase, " + saved.getName() + "!");
    }

    @Override
    public ApiResponse<?> login(LoginRequest request) {
        log.debug("Login attempt for email: {}", request.getEmail());
        try {
            // AuthenticationManager internally:
            //   a. Calls CustomUserDetailsService.loadUserByUsername(email)
            //   b. Loads user from DB
            //   c. Compares provided password with stored BCrypt hash
            //   d. Throws BadCredentialsException if password is wrong
            //   e. Returns Authentication object if successful
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            // Get the authenticated user from Authentication object
            // getPrincipal() returns the UserDetails object that was loaded
            // by CustomUserDetailsService — which is our CustomUserDetails
            CustomUserDetails userDetails =
                    (CustomUserDetails) authentication.getPrincipal();
            // Generate JWT token for this user
            // Token contains email as subject and expires in 24 hours
            String token = jwtUtils.generateToken(userDetails);
            // Build the response
            // getUser() gives us back the original User entity
            // from inside CustomUserDetails wrapper
            AuthResponse authResponse = userMapper.toAuthResponse(userDetails.getUser(), token);
            log.info("User logged in successfully: {}", request.getEmail());
            return ResponseUtil.getSuccessResponseWithData(authResponse, "Welcome back, " + userDetails.getUser().getName() + "!");
        } catch (BadCredentialsException ex) {
            // BadCredentialsException is thrown by AuthenticationManager
            // when email does not exist or password is wrong
            // We give a deliberately vague message —
            // never tell the caller which one is wrong
            // as that helps attackers know valid emails
            log.warn("Failed login attempt for email: {}", request.getEmail());
            throw new ApiException("Invalid email or password", HttpStatus.UNAUTHORIZED);
        }
    }
}
