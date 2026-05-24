package com.codealpha.hotel_management_system.controller;

import com.codealpha.hotel_management_system.config.jwt.CustomUserDetails;
import com.codealpha.hotel_management_system.constant.ApiConstant;
import com.codealpha.hotel_management_system.core.controller.BaseController;
import com.codealpha.hotel_management_system.dto.requests.PaymentRequest;
import com.codealpha.hotel_management_system.dto.response.ApiResponse;
import com.codealpha.hotel_management_system.service.PaymentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Payments", description = "Payment processing endpoints")
@RestController
@RequestMapping(ApiConstant.API + ApiConstant.SLASH + ApiConstant.PAYMENTS)
@RequiredArgsConstructor
public class PaymentController extends BaseController {
    private final PaymentService paymentService;

    @PostMapping(ApiConstant.PAY + ApiConstant.SLASH + "{reservationId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<?>> processPayment(@PathVariable Integer reservationId, @RequestBody @Valid PaymentRequest request, @AuthenticationPrincipal CustomUserDetails userDetails) {
        return created(paymentService.processPayment(reservationId, request, userDetails.getUsername()));
    }

    @GetMapping(ApiConstant.VIEW + ApiConstant.SLASH + "{paymentId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<?>> getPaymentById(@PathVariable Integer paymentId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ok(paymentService.getPaymentById(paymentId, userDetails.getUsername()));
    }

    @GetMapping(ApiConstant.RESERVATIONS + ApiConstant.SLASH + "{reservationId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<?>> getPaymentByReservationId(@PathVariable Integer reservationId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ok(paymentService.getPaymentByReservationId(reservationId, userDetails.getUsername()));
    }

    // Admin only — process a refund for a payment
    // PATCH because we are updating payment status to REFUNDED only Also updates reservation to CANCELLED and room back to AVAILABLE automatically in service
    @PatchMapping(ApiConstant.REFUND + ApiConstant.SLASH + "{paymentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> refundPayment(@PathVariable Integer paymentId) {
        return ok(paymentService.refundPayment(paymentId));
    }
}
