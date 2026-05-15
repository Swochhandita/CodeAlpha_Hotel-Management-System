package com.codealpha.hotel_management_system.mapper;

import com.codealpha.hotel_management_system.dto.requests.RoomRequest;
import com.codealpha.hotel_management_system.dto.requests.RoomStatusUpdateRequest;
import com.codealpha.hotel_management_system.dto.response.RoomResponse;
import com.codealpha.hotel_management_system.entity.Hotel;
import com.codealpha.hotel_management_system.entity.Room;
import com.codealpha.hotel_management_system.enums.RoomStatus;
import org.springframework.stereotype.Component;

@Component
public class RoomMapper {
    public Room toEntity(RoomRequest request, Hotel hotel) {
        Room room = new Room();
        room.setHotel(hotel);
        room.setRoomNumber(request.getRoomNumber());
        room.setRoomType(request.getRoomType());
        room.setPricePerNight(request.getPricePerNight());
        // if maxOccupancy not provided default to 2
        room.setMaxOccupancy(request.getMaxOccupancy() != null ? request.getMaxOccupancy() : 2);
        room.setDescription(request.getDescription());
        // every new room is AVAILABLE by default
        // only admin can change it to MAINTENANCE via status update endpoint
        room.setStatus(RoomStatus.AVAILABLE);
        return room;
    }

    public RoomResponse toResponse(Room room) {
        RoomResponse response = new RoomResponse();
        response.setId(room.getId());
        response.setRoomNumber(room.getRoomNumber());
        response.setRoomType(room.getRoomType());
        response.setStatus(room.getStatus());
        response.setPricePerNight(room.getPricePerNight());
        response.setMaxOccupancy(room.getMaxOccupancy());
        response.setDescription(room.getDescription());
        // navigate to hotel object for hotel context
        // hotel is LAZY loaded so it must be accessed within
        // an active transaction —  service is @Tensureransactional
        response.setHotelId(room.getHotel().getId());
        response.setHotelName(room.getHotel().getName());
        return response;
    }
    //no change in the hotel and the status here
    public Room updateEntity(RoomRequest request, Room room) {
        if (request.getRoomNumber() != null) {
            room.setRoomNumber(request.getRoomNumber());
        }
        if (request.getRoomType() != null) {
            room.setRoomType(request.getRoomType());
        }
        if (request.getPricePerNight() != null) {
            room.setPricePerNight(request.getPricePerNight());
        }
        if (request.getMaxOccupancy() != null) {
            room.setMaxOccupancy(request.getMaxOccupancy());
        }
        if (request.getDescription() != null) {
            room.setDescription(request.getDescription());
        }
        return room;
    }

    public Room updateStatus(RoomStatusUpdateRequest request, Room room) {
        room.setStatus(request.getStatus());
        return room;
    }
}
