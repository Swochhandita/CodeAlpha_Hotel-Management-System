package com.codealpha.hotel_management_system.controller;

import com.codealpha.hotel_management_system.constant.ApiConstant;
import com.codealpha.hotel_management_system.core.controller.BaseController;
import com.codealpha.hotel_management_system.dto.requests.LoginRequest;
import com.codealpha.hotel_management_system.dto.requests.RegisterRequest;
import com.codealpha.hotel_management_system.dto.response.ApiResponse;
import com.codealpha.hotel_management_system.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication", description = "Register and login endpoints")
@RestController//means every method returns data directly as JSON ,not a view/template
@RequestMapping(ApiConstant.API+ApiConstant.SLASH+ApiConstant.AUTH)
@RequiredArgsConstructor
public class AuthController extends BaseController {

    private final AuthService authService;

    @PostMapping(ApiConstant.REGISTER)
    public ResponseEntity<ApiResponse<?>> register(@RequestBody @Valid RegisterRequest request) {
        return created(authService.register(request));
    }

    @PostMapping(ApiConstant.LOGIN)
    public ResponseEntity<ApiResponse<?>> login(@RequestBody @Valid LoginRequest request) {
        return ok(authService.login(request));
    }
}
