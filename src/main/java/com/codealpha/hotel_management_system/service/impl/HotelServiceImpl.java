package com.codealpha.hotel_management_system.service.impl;

import com.codealpha.hotel_management_system.dto.requests.HotelRequest;
import com.codealpha.hotel_management_system.dto.response.ApiResponse;
import com.codealpha.hotel_management_system.dto.response.HotelResponse;
import com.codealpha.hotel_management_system.dto.response.PagedResponse;
import com.codealpha.hotel_management_system.entity.Hotel;
import com.codealpha.hotel_management_system.exception.DuplicateResourceException;
import com.codealpha.hotel_management_system.exception.ResourceNotFoundException;
import com.codealpha.hotel_management_system.mapper.HotelMapper;
import com.codealpha.hotel_management_system.repository.HotelRepository;
import com.codealpha.hotel_management_system.service.HotelService;
import com.codealpha.hotel_management_system.utils.ResponseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {
    private  final HotelMapper hotelMapper;
    private final HotelRepository hotelRepository;


    @Override
    public ApiResponse<?> createHotel(HotelRequest request) {
        log.debug("Creating hotel with name: {}", request.getName());

        // Check if hotel with same name already exists in same city
        // existsByNameIgnoreCaseAndCityIgnoreCase is case-insensitive
        // "hotel yak" and "Hotel Yak" are treated as same
        if (hotelRepository.existsByNameIgnoreCaseAndCityIgnoreCase(request.getName(), request.getCity())) {
            throw new DuplicateResourceException("Hotel already exists with name: " + request.getName() + " in city: " + request.getCity());
        }
        Hotel hotel = hotelMapper.toEntity(request);
        Hotel saved = hotelRepository.save(hotel);
        log.info("Hotel created successfully with id: {}", saved.getId());
        HotelResponse response = hotelMapper.toResponse(saved);
        return ResponseUtil.getCreatedResponseWithData(response, "Hotel created successfully");
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<?> getHotelById(Integer id) {
        log.debug("Fetching hotel with id: {}", id);
        Hotel hotel = hotelRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + id));
        return ResponseUtil.getSuccessResponseWithData(hotelMapper.toResponse(hotel), "Hotel fetched successfully");
    }

    @Override
    public ApiResponse<?> getAllHotels(Pageable pageable) {
        Page<Hotel> hotelPage = hotelRepository.findAll(pageable);
        PagedResponse<HotelResponse> response = PagedResponse.fromPage(hotelPage, hotelMapper::toResponse);
        return ResponseUtil.getSuccessResponseWithData(response, "Hotels fetched successfully");
    }

    @Override
    public ApiResponse<?> getHotelsByCity(String city, Pageable pageable) {
        log.debug("Fetching hotels in city: {}", city);
        Page<Hotel> hotelPage = hotelRepository.findByCityIgnoreCase(city, pageable);
        PagedResponse<HotelResponse> response = PagedResponse.fromPage(hotelPage, hotelMapper::toResponse);
        return ResponseUtil.getSuccessResponseWithData(response, "Hotels in " + city + " fetched successfully");
    }

    @Override
    public ApiResponse<?> updateHotel(Integer id, HotelRequest request) {
        log.debug("Updating hotel with id: {}", id);
        Hotel hotel = hotelRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + id));
        Hotel updated = hotelMapper.updateEntity(request, hotel);
        // @UpdateTimestamp on updatedAt fires automatically
        Hotel saved = hotelRepository.save(updated);
        log.info("Hotel updated successfully with id: {}", saved.getId());
        return ResponseUtil.getSuccessResponseWithData(hotelMapper.toResponse(saved), "Hotel updated successfully");
    }

    @Override
    public ApiResponse<?> deleteHotel(Integer id) {
        log.debug("Deleting hotel with id: {}", id);
        Hotel hotel = hotelRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + id));
        hotelRepository.delete(hotel);
        log.info("Hotel deleted successfully with id: {}", id);
        return ResponseUtil.getSuccessResponse("Hotel deleted successfully");
    }
}
