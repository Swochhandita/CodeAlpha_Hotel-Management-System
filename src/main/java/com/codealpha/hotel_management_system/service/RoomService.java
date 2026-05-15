package com.codealpha.hotel_management_system.service;

import com.codealpha.hotel_management_system.dto.requests.RoomRequest;
import com.codealpha.hotel_management_system.dto.requests.RoomStatusUpdateRequest;
import com.codealpha.hotel_management_system.dto.response.ApiResponse;
import org.springframework.data.domain.Pageable;

public interface RoomService {
    ApiResponse<?> addRoom(Integer hotelId, RoomRequest request);
    ApiResponse<?> getRoomById(Integer hotelId, Integer roomId);
    ApiResponse<?> getRoomsByHotel(Integer hotelId, Pageable pageable);
    ApiResponse<?> getAvailableRooms(Integer hotelId, Pageable pageable);
    ApiResponse<?> updateRoom(Integer hotelId, Integer roomId, RoomRequest request);
    // Update only the status of a room
    // Example: AVAILABLE → MAINTENANCE or MAINTENANCE → AVAILABLE
    // Used by: admin only
    ApiResponse<?> updateRoomStatus(Integer hotelId, Integer roomId, RoomStatusUpdateRequest request);
    ApiResponse<?> deleteRoom(Integer hotelId, Integer roomId);
}
