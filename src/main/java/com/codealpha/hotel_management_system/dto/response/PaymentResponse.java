package com.codealpha.hotel_management_system.dto.response;

import com.codealpha.hotel_management_system.enums.PaymentMethod;
import com.codealpha.hotel_management_system.enums.PaymentStatus;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
public class PaymentResponse {
    private Long id;
    private Long reservationId;
    // Enough context so the client knows WHAT was paid for
    private String guestName;
    private String roomNumber;
    private String hotelName;
    // the payment info
    private BigDecimal amount;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private String transactionRef;
    private LocalDateTime paidAt;      // null if not yet successful
    private LocalDateTime createdAt;
}
