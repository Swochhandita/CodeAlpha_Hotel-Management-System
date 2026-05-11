package com.codealpha.hotel_management_system.dto.requests;

import com.codealpha.hotel_management_system.enums.RoomType;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Data
public class RoomRequest {
    @NotBlank(message = "Room number is required")
    private String roomNumber;

    @NotNull(message = "Room type is required")
    private RoomType roomType;

    @NotNull(message = "Price per night is required")
    @DecimalMin(value = "0.01", message = "Price must be positive")
    private BigDecimal pricePerNight;

    @Min(value = 1) @Max(value = 10)
    private Integer maxOccupancy;

    private String description;
}
