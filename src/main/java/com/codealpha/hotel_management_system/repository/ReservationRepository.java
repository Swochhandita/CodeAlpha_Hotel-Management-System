package com.codealpha.hotel_management_system.repository;

import com.codealpha.hotel_management_system.entity.Reservation;
import com.codealpha.hotel_management_system.enums.ReservationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    Optional<Reservation> findByUserIdAndStatus(Integer userId, ReservationStatus status);
    Optional<Reservation> findByRoomId(Integer roomId);
    Optional<Reservation> findByIdAndUserId(Integer id, Integer userId);
    /*
     * Core overlap check — used before creating a reservation.
     * Checks if any CONFIRMED or PENDING reservation exists for this room
     * where the date ranges overlap.
     *
     * Overlap condition:
     * existing.checkIn < requested.checkOut
     * AND
     * existing.checkOut > requested.checkIn
     *
     * This covers all overlap cases:
     *   - New booking starts during existing booking
     *   - New booking ends during existing booking
     *   - New booking completely contains existing booking
     *   - Existing booking completely contains new booking
     */
    @Query("""
            SELECT COUNT(r) > 0 FROM Reservation r
            WHERE r.room.id = :roomId
            AND r.status IN ('PENDING', 'CONFIRMED')
            AND r.checkInDate < :checkOut
            AND r.checkOutDate > :checkIn
            """)
    boolean existsOverlappingReservation(@Param("roomId") Long roomId, @Param("checkIn") LocalDate checkIn, @Param("checkOut") LocalDate checkOut);
    long countByStatus(ReservationStatus status);
    Page<Reservation> findByUserId(Integer id, Pageable pageable);
}
