package com.codealpha.hotel_management_system.dto.requests;

import com.codealpha.hotel_management_system.enums.RoomStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RoomStatusUpdateRequest {
    @NotNull(message = "Room status is required")
    private RoomStatus status;

    // Optional note for "Broken AC", "Carpet replacement" etc.
    private String note;
}
