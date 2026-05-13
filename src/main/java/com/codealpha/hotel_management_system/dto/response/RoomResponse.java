package com.codealpha.hotel_management_system.dto.response;
import com.codealpha.hotel_management_system.enums.RoomStatus;
import com.codealpha.hotel_management_system.enums.RoomType;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class RoomResponse {
    private Integer id;
    private Integer hotelId;
    private String hotelName;          // avoids a second API call from the client
    private String roomNumber;
    private RoomType roomType;
    private RoomStatus status;
    private BigDecimal pricePerNight;
    private Integer maxOccupancy;
    private String description;
}
