package com.codealpha.hotel_management_system.mapper;

import com.codealpha.hotel_management_system.dto.requests.HotelRequest;
import com.codealpha.hotel_management_system.dto.response.HotelResponse;
import com.codealpha.hotel_management_system.entity.Hotel;
import com.codealpha.hotel_management_system.enums.RoomStatus;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class HotelMapper {
    public Hotel toEntity(HotelRequest request) {
        Hotel hotel = new Hotel();
        hotel.setName(request.getName());
        hotel.setAddress(request.getAddress());
        hotel.setCity(request.getCity());
        hotel.setCountry(request.getCountry() != null ? request.getCountry() : "Nepal");
        hotel.setDescription(request.getDescription());
        hotel.setPhone(request.getPhone());
        hotel.setEmail(request.getEmail());
        return hotel;
    }

    public HotelResponse toResponse(Hotel hotel) {
        HotelResponse response = new HotelResponse();
        response.setId(hotel.getId());
        response.setName(hotel.getName());
        response.setAddress(hotel.getAddress());
        response.setCity(hotel.getCity());
        response.setCountry(hotel.getCountry());
        response.setDescription(hotel.getDescription());
        response.setPhone(hotel.getPhone());
        response.setEmail(hotel.getEmail());
        response.setTotalRooms(hotel.getRooms().size());//count total rooms in the hotel
        // count only rooms with AVAILABLE status
        // used in search results so guest knows how many rooms are bookable
        response.setAvailableRooms((int) hotel.getRooms()
                .stream()
                .filter(room -> room.getStatus() == RoomStatus.AVAILABLE)
                .count());
        return response;
    }
    //Update is also done here
    public Hotel updateEntity(HotelRequest request, Hotel hotel) {
        if (request.getName() != null) {
            hotel.setName(request.getName());
        }
        if (request.getAddress() != null) {
            hotel.setAddress(request.getAddress());
        }
        if (request.getCity() != null) {
            hotel.setCity(request.getCity());
        }
        if (request.getCountry() != null) {
            hotel.setCountry(request.getCountry());
        }
        if (request.getDescription() != null) {
            hotel.setDescription(request.getDescription());
        }
        if (request.getPhone() != null) {
            hotel.setPhone(request.getPhone());
        }
        if (request.getEmail() != null) {
            hotel.setEmail(request.getEmail());
        }
        return hotel;
    }
}
