package com.codealpha.hotel_management_system.dto.response;

import lombok.Data;
@Data
public class HotelResponse {
    private Long id;
    private String name;
    private String address;
    private String city;
    private String country;
    private String description;
    private Integer starRating;
    private String phone;
    private String email;
    private Integer totalRooms;         // derived — count of rooms in this hotel
    private Integer availableRooms;     // derived — count of AVAILABLE rooms theough lists of the rooms.
}
