package com.codealpha.hotel_management_system.mapper;

import com.codealpha.hotel_management_system.dto.requests.ReservationRequest;
import com.codealpha.hotel_management_system.dto.requests.ReservationStatusUpdateRequest;
import com.codealpha.hotel_management_system.dto.response.ReservationResponse;
import com.codealpha.hotel_management_system.entity.Reservation;
import com.codealpha.hotel_management_system.entity.Room;
import com.codealpha.hotel_management_system.entity.User;
import com.codealpha.hotel_management_system.enums.ReservationStatus;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;

@Component
public class ReservationMapper {
    public Reservation toEntity(ReservationRequest request, User user, Room room) {
        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setRoom(room);
        reservation.setCheckInDate(request.getCheckInDate());
        reservation.setCheckOutDate(request.getCheckOutDate());
        reservation.setStatus(ReservationStatus.PENDING);
        // calculate nights between checkIn and checkOut
        // Example: April 5 → April 8 = 3 nights
        long numberOfNights = ChronoUnit.DAYS.between(request.getCheckInDate(), request.getCheckOutDate());
        // totalPrice = pricePerNight × numberOfNights
        // BigDecimal multiplication avoids floating point errors
        BigDecimal totalPrice = room.getPricePerNight().multiply(BigDecimal.valueOf(numberOfNights));
        reservation.setTotalPrice(totalPrice);
        return reservation;
    }
    // Reservation entity → ReservationResponse
    // Called in ReservationService for all read operations
    // Flattens nested user, room, hotel into single response
    // so client does not need multiple API calls excluding for the payment
    public ReservationResponse toResponse(Reservation reservation) {
        ReservationResponse response = new ReservationResponse();
        response.setId(reservation.getId());
        response.setStatus(reservation.getStatus());
        response.setCheckInDate(reservation.getCheckInDate());
        response.setCheckOutDate(reservation.getCheckOutDate());
        response.setTotalPrice(reservation.getTotalPrice());
        response.setCreatedAt(reservation.getCreatedAt());
        response.setUpdatedAt(reservation.getUpdatedAt());
        // numberOfNights derived from dates — not stored in DB
        long numberOfNights = ChronoUnit.DAYS.between(reservation.getCheckInDate(), reservation.getCheckOutDate());
        response.setNumberOfNights((int) numberOfNights);
        // flat user fields — only display info, never password
        response.setUserId(reservation.getUser().getId());
        response.setGuestName(reservation.getUser().getName());
        response.setGuestEmail(reservation.getUser().getEmail());
        // flat room fields
        response.setRoomId(reservation.getRoom().getId());
        response.setRoomNumber(reservation.getRoom().getRoomNumber());
        response.setRoomType(reservation.getRoom().getRoomType().name());
        response.setPricePerNight(reservation.getRoom().getPricePerNight());
        // flat hotel fields — navigates room → hotel
        // room.getHotel() is LAZY so service must be @Transactional
        response.setHotelId(reservation.getRoom().getHotel().getId());
        response.setHotelName(reservation.getRoom().getHotel().getName());
        response.setHotelCity(reservation.getRoom().getHotel().getCity());
        return response;
    }
    public Reservation updateStatus(ReservationStatusUpdateRequest request, Reservation reservation) {
        reservation.setStatus(request.getStatus());
        return reservation;
    }
}
