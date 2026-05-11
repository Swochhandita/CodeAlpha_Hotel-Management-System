package com.codealpha.hotel_management_system.dto.requests;

import com.codealpha.hotel_management_system.enums.ReservationStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class ReservationStatusUpdateRequest {
    @NotNull(message = "Status is required")
    private ReservationStatus status;

    private String reason;
}
