package com.codealpha.hotel_management_system.repository;

import com.codealpha.hotel_management_system.entity.Payment;
import com.codealpha.hotel_management_system.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    Optional<Payment> findByReservationId(Integer reservationId);
    // Used to prevent double payment — check before processing
    boolean existsByReservationId(Integer reservationId);
    boolean existsByReservationIdAndPaymentStatus(Integer reservationId, PaymentStatus paymentStatus);
}
