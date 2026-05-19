package com.codealpha.hotel_management_system.service;

import com.codealpha.hotel_management_system.dto.requests.ReservationRequest;
import com.codealpha.hotel_management_system.dto.requests.ReservationStatusUpdateRequest;
import com.codealpha.hotel_management_system.dto.response.ApiResponse;
import org.springframework.data.domain.Pageable;

public interface ReservationService {
    ApiResponse<?> createReservation(ReservationRequest request, String email);
    // Used by: admin — can see any reservation
    //          guest — can only see their own
    ApiResponse<?> getReservationById(Integer id, String email);
    // Used by: admin only — sees every reservation in the system
    ApiResponse<?> getAllReservations(Pageable pageable);
    // Get all reservations of the currently logged in guest and one guest can have many reservations as well
    // Used by: guest — sees only their own reservations
    // email comes from JWT token
    ApiResponse<?> getMyReservations(String email, Pageable pageable);
    ApiResponse<?> updateReservationStatus(Integer id, ReservationStatusUpdateRequest request);
    ApiResponse<?> cancelReservation(Integer id, String email);
    byte[] exportReservationPdf(Integer reservationId, String email);
}
