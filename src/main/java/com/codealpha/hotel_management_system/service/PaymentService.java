package com.codealpha.hotel_management_system.service;

import com.codealpha.hotel_management_system.dto.requests.PaymentRequest;
import com.codealpha.hotel_management_system.dto.response.ApiResponse;

public interface PaymentService {
    ApiResponse<?> processPayment(Integer reservationId, PaymentRequest request, String email);
    ApiResponse<?> getPaymentByReservationId(Integer reservationId, String email);
    ApiResponse<?> getPaymentById(Integer paymentId, String email);
    // Example: guest cancels confirmed reservation — admin processes refund
    ApiResponse<?> refundPayment(Integer paymentId);
}
