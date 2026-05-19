package com.codealpha.hotel_management_system.controller;

import com.codealpha.hotel_management_system.config.jwt.CustomUserDetails;
import com.codealpha.hotel_management_system.constant.ApiConstant;
import com.codealpha.hotel_management_system.core.controller.BaseController;
import com.codealpha.hotel_management_system.dto.requests.ReservationRequest;
import com.codealpha.hotel_management_system.dto.requests.ReservationStatusUpdateRequest;
import com.codealpha.hotel_management_system.dto.response.ApiResponse;
import com.codealpha.hotel_management_system.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiConstant.API + ApiConstant.SLASH + ApiConstant.RESERVATIONS)
public class ReservationController extends BaseController {

    private final ReservationService reservationService;

    @PostMapping(ApiConstant.CREATE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<?>> createReservation(@RequestBody @Valid ReservationRequest request, @AuthenticationPrincipal CustomUserDetails userDetails) {
        return created(reservationService.createReservation(request, userDetails.getUsername()));
    }

    // Authenticated — both admin and guest can call this but guest can only view their own reservation
    // ownership check is done in service layer
    @GetMapping(ApiConstant.VIEW + ApiConstant.SLASH + "{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<?>> getReservationById(@PathVariable Integer id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ok(reservationService.getReservationById(id, userDetails.getUsername()));
    }

    @GetMapping(ApiConstant.LIST)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> getAllReservations(@PageableDefault(page = 0, size = 10, sort = "id") Pageable pageable) {
        return ok(reservationService.getAllReservations(pageable));
    }

    @GetMapping(ApiConstant.ME)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<?>> getMyReservations(@AuthenticationPrincipal CustomUserDetails userDetails, @PageableDefault(page = 0, size = 10, sort = "id") Pageable pageable) {
        return ok(reservationService.getMyReservations(userDetails.getUsername(), pageable));
    }

    @PatchMapping(ApiConstant.STATUS + ApiConstant.SLASH + "{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> updateReservationStatus(@PathVariable Integer id, @RequestBody @Valid ReservationStatusUpdateRequest request) {
        return ok(reservationService.updateReservationStatus(id, request));
    }

    @PatchMapping(ApiConstant.CANCEL + ApiConstant.SLASH + "{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<?>> cancelReservation(@PathVariable Integer id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ok(reservationService.cancelReservation(id, userDetails.getUsername()));// userDetails here is used to know whether this user is allowed to cancel the reservation or not cause we can't let anyone cancel other's reservation so for the conformation and security concern we use it here.
    }

    @GetMapping(ApiConstant.EXPORT + ApiConstant.SLASH + "{reservationId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<byte[]> exportReservationPdf(@PathVariable Integer reservationId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        byte[] pdfBytes = reservationService.exportReservationPdf(reservationId, userDetails.getUsername());
        String fileName = "reservation_" + reservationId + ".pdf";
        return file(pdfBytes, fileName);
    }
}
