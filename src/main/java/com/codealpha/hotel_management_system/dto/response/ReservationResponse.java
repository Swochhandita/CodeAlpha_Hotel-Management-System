package com.codealpha.hotel_management_system.dto.response;

import com.codealpha.hotel_management_system.enums.ReservationStatus;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ReservationResponse {
    private Integer id;
    private Integer userId;
    private String guestName;    // user.name
    private String guestEmail;   // user.email
    // Flat room + hotel info — same reasoning: avoid deep nesting using the static class
    private Integer roomId;
    private String roomNumber;
    private String roomType;     // String not enum — cleaner JSON ("DELUXE" not {name: "DELUXE"})
    private Integer hotelId;
    private String hotelName;
    private String hotelCity;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    /*
     * numberOfNights: derived from checkOut - checkIn.
     * Calculated in the mapper — not stored in DB.
     * Saves the client from doing date math.
     */
    private Integer numberOfNights;
    private BigDecimal pricePerNight;   // room's price at time of booking
    private BigDecimal totalPrice;      // pricePerNight × numberOfNights
    private ReservationStatus status;
    private String specialRequests;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
