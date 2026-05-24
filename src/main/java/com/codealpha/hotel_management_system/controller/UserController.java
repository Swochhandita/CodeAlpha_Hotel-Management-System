package com.codealpha.hotel_management_system.controller;

import com.codealpha.hotel_management_system.config.jwt.CustomUserDetails;
import com.codealpha.hotel_management_system.constant.ApiConstant;
import com.codealpha.hotel_management_system.core.controller.BaseController;
import com.codealpha.hotel_management_system.dto.response.ApiResponse;
import com.codealpha.hotel_management_system.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Users", description = "User management endpoints")
@RestController
@RequestMapping(ApiConstant.API + ApiConstant.SLASH + ApiConstant.USERS)
@RequiredArgsConstructor
public class UserController extends BaseController {
    private final UserService userService;

    @GetMapping(ApiConstant.LIST)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> list(@PageableDefault(page = 0, size = 10, sort = "id") Pageable pageable) {
        return ok(userService.list(pageable));
    }

    @GetMapping(ApiConstant.VIEW + ApiConstant.SLASH + "{id}")
    @PreAuthorize("hasRole('ADMIN')")//Admin only can view any user
    public ResponseEntity<ApiResponse<?>> getUserById(@PathVariable Integer id) {
        return ok(userService.getUserById(id));
    }

    @GetMapping(ApiConstant.ME)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<?>> getMyProfile(@AuthenticationPrincipal CustomUserDetails userDetails) {
        // Extract email from CustomUserDetails pass it to service — service loads full profile from DB
        return ok(userService.getMyProfile(userDetails.getUsername()));
    }

    @DeleteMapping(ApiConstant.DELETE + ApiConstant.SLASH + "{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> deleteUser(@PathVariable Integer id) {
        return noContent(userService.deleteUser(id));
    }

    @PatchMapping("promote" + ApiConstant.SLASH + "{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> promoteToAdmin(@PathVariable Integer id) {
        return ok(userService.promoteToAdmin(id));
    }
}
