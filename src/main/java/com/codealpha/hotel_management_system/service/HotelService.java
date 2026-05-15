package com.codealpha.hotel_management_system.service;

import com.codealpha.hotel_management_system.dto.requests.HotelRequest;
import com.codealpha.hotel_management_system.dto.response.ApiResponse;
import org.springframework.data.domain.Pageable;

public interface HotelService {

    ApiResponse<?> createHotel(HotelRequest request);
    ApiResponse<?> getHotelById(Integer id);
    ApiResponse<?> getAllHotels(Pageable pageable);
    ApiResponse<?> getHotelsByCity(String city, Pageable pageable);
    ApiResponse<?> updateHotel(Integer id, HotelRequest request);
    ApiResponse<?> deleteHotel(Integer id);
}
