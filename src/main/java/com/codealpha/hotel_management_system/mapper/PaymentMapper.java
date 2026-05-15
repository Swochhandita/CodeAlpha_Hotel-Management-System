package com.codealpha.hotel_management_system.mapper;

import com.codealpha.hotel_management_system.dto.requests.PaymentRequest;
import com.codealpha.hotel_management_system.dto.response.PaymentResponse;
import com.codealpha.hotel_management_system.entity.Payment;
import com.codealpha.hotel_management_system.entity.Reservation;
import com.codealpha.hotel_management_system.enums.PaymentStatus;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
public class PaymentMapper {
    public Payment toEntity(PaymentRequest request, Reservation reservation, String transactionRef) {
        Payment payment = new Payment();
        payment.setReservation(reservation);
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setPaymentStatus(PaymentStatus.PENDING);
        payment.setAmount(reservation.getTotalPrice());
        payment.setTransactionRef(transactionRef);
        payment.setPaidAt(null);
        return payment;
    }

    public PaymentResponse toResponse(Payment payment) {
        PaymentResponse response = new PaymentResponse();
        response.setId(payment.getId());
        response.setAmount(payment.getAmount());
        response.setPaymentMethod(payment.getPaymentMethod());
        response.setPaymentStatus(payment.getPaymentStatus());
        response.setTransactionRef(payment.getTransactionRef());
        response.setPaidAt(payment.getPaidAt());
        response.setCreatedAt(payment.getCreatedAt());
        // reservation context — navigate payment → reservation
        response.setReservationId(payment.getReservation().getId());
        // guest name from reservation → user
        response.setGuestName(payment.getReservation().getUser().getName());
        // room number from reservation → room
        response.setRoomNumber(payment.getReservation().getRoom().getRoomNumber());
        // hotel name from reservation → room → hotel
        // all LAZY loaded so service must be @Transactional
        response.setHotelName(payment.getReservation().getRoom().getHotel().getName());
        return response;
    }

    public Payment markAsSuccess(Payment payment) {
        payment.setPaymentStatus(PaymentStatus.SUCCESS);
        // paidAt records exact moment payment was confirmed
        payment.setPaidAt(LocalDateTime.now());
        return payment;
    }

    public Payment markAsFailed(Payment payment) {
        payment.setPaymentStatus(PaymentStatus.FAILED);
        return payment;
    }

    public Payment markAsRefunded(Payment payment) {
        payment.setPaymentStatus(PaymentStatus.REFUNDED);
        return payment;
    }
}
