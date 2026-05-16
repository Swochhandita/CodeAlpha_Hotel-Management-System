package com.codealpha.hotel_management_system.service.impl;

import com.codealpha.hotel_management_system.dto.requests.PaymentRequest;
import com.codealpha.hotel_management_system.dto.response.ApiResponse;
import com.codealpha.hotel_management_system.entity.Payment;
import com.codealpha.hotel_management_system.entity.Reservation;
import com.codealpha.hotel_management_system.enums.PaymentStatus;
import com.codealpha.hotel_management_system.enums.ReservationStatus;
import com.codealpha.hotel_management_system.enums.RoomStatus;
import com.codealpha.hotel_management_system.exception.ApiException;
import com.codealpha.hotel_management_system.exception.ResourceNotFoundException;
import com.codealpha.hotel_management_system.exception.UnauthorizedException;
import com.codealpha.hotel_management_system.mapper.PaymentMapper;
import com.codealpha.hotel_management_system.repository.PaymentRepository;
import com.codealpha.hotel_management_system.repository.ReservationRepository;
import com.codealpha.hotel_management_system.service.PaymentService;
import com.codealpha.hotel_management_system.utils.ResponseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final ReservationRepository reservationRepository;
    private final PaymentMapper paymentMapper;

    @Override
    @Transactional
    public ApiResponse<?> processPayment(Integer reservationId, PaymentRequest request, String email) {
        log.debug("Processing payment for reservation: {}", reservationId);
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id: " + reservationId));
        // Step 2: Ownership check — guest can only pay for their own reservation not someone else's
        if (!reservation.getUser().getEmail().equals(email)) {
            throw new UnauthorizedException("You are not authorized to pay for this reservation");
        }
        if (reservation.getStatus() == ReservationStatus.CANCELLED) {
            throw new ApiException("Cannot process payment for a cancelled reservation", HttpStatus.BAD_REQUEST);
        }
        if (reservation.getStatus() == ReservationStatus.COMPLETED) {
            throw new ApiException("Cannot process payment for a completed reservation", HttpStatus.BAD_REQUEST);
        }
        // Step 5: Check if payment already exists for this reservation
        if (paymentRepository.existsByReservationId(reservationId)) {
            Payment existingPayment = paymentRepository.findByReservationId(reservationId).orElseThrow(() -> new ResourceNotFoundException("Payment not found for reservation: " + reservationId));
            if (existingPayment.getPaymentStatus() == PaymentStatus.SUCCESS) {
                throw new ApiException("Payment already completed for this reservation", HttpStatus.CONFLICT);
            }
        }
        // Step 6: Generate a transaction reference if not provided
        // For CASH payments transactionRef will be null from client
        // We generate a simulated reference for all cases In production this would come from the payment gateway response
        String transactionRef = request.getTransactionRef() != null ? request.getTransactionRef() : "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        // Amount is taken from reservation.totalPrice in the mapper —
        // client never sends amount to prevent price tampering
        Payment payment = paymentMapper.toEntity(request, reservation, transactionRef);
        // Step 8: Simulate payment processing
        // In production — call eSewa/Khalti API here  and check the response before marking as SUCCESS
        Payment processedPayment = paymentMapper.markAsSuccess(payment);
        Payment saved = paymentRepository.save(processedPayment);
        // Step 10: Update reservation status to CONFIRMED
        // Payment is done — reservation is now confirmed
        reservation.setStatus(ReservationStatus.CONFIRMED);
        // Step 11: Update room status to OCCUPIED
        // Room is now taken — cannot be booked by anyone else
        reservation.getRoom().setStatus(RoomStatus.OCCUPIED);
        reservationRepository.save(reservation);
        log.info("Payment processed successfully for reservation: {}", reservationId);
        return ResponseUtil.getCreatedResponseWithData(paymentMapper.toResponse(saved), "Payment processed successfully via " + saved.getPaymentMethod().name());
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<?> getPaymentByReservationId(Integer reservationId, String email) {
        log.debug("Fetching payment for reservation: {}", reservationId);
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id: " + reservationId));
        // Ownership check — guest can only see their own payment
        if (!reservation.getUser().getEmail().equals(email)) {
            throw new UnauthorizedException("You are not authorized to view this payment");
        }
        Payment payment = paymentRepository.findByReservationId(reservationId).orElseThrow(() -> new ResourceNotFoundException("Payment not found for reservation: " + reservationId));
        return ResponseUtil.getSuccessResponseWithData(paymentMapper.toResponse(payment), "Payment fetched successfully");
    }

    @Override
    public ApiResponse<?> getPaymentById(Integer reservationId, String email) {
        log.debug("Fetching payment for reservation: {}", reservationId);
        // Load reservation first for ownership check
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id: " + reservationId));
        // Ownership check — guest can only see their own payment
        if (!reservation.getUser().getEmail().equals(email)) {
            throw new UnauthorizedException("You are not authorized to view this payment");
        }
        // Load payment by reservation ID
        Payment payment = paymentRepository.findByReservationId(reservationId).orElseThrow(() -> new ResourceNotFoundException("Payment not found for reservation: " + reservationId));
        return ResponseUtil.getSuccessResponseWithData(paymentMapper.toResponse(payment), "Payment fetched successfully");
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<?> refundPayment(Integer paymentId) {
        log.debug("Processing refund for payment: {}", paymentId);
        Payment payment = paymentRepository.findById(paymentId).orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + paymentId));
        // Can only refund a SUCCESS payment
        // Cannot refund a PENDING, FAILED, or already REFUNDED payment
        if (payment.getPaymentStatus() != PaymentStatus.SUCCESS) {
            throw new ApiException("Only successful payments can be refunded", HttpStatus.BAD_REQUEST);
        }
        // Mark payment as REFUNDED via mapper
        Payment refunded = paymentMapper.markAsRefunded(payment);
        Payment saved = paymentRepository.save(refunded);
        Reservation reservation = saved.getReservation();
        reservation.setStatus(ReservationStatus.CANCELLED);
        // Set room back to AVAILABLE SO  Room can now be booked by someone else
        reservation.getRoom().setStatus(RoomStatus.AVAILABLE);
        reservationRepository.save(reservation);
        log.info("Payment refunded successfully for payment: {}", paymentId);
        return ResponseUtil.getSuccessResponseWithData(paymentMapper.toResponse(saved), "Payment refunded successfully");
    }
}
