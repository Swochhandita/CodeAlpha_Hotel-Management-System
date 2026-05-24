package com.codealpha.hotel_management_system.controller;

import com.codealpha.hotel_management_system.constant.ApiConstant;
import com.codealpha.hotel_management_system.core.controller.BaseController;
import com.codealpha.hotel_management_system.dto.requests.HotelRequest;
import com.codealpha.hotel_management_system.dto.response.ApiResponse;
import com.codealpha.hotel_management_system.service.HotelService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Hotels", description = "Hotel management endpoints")
@RestController
@RequestMapping(ApiConstant.API + ApiConstant.SLASH + ApiConstant.HOTELS)
@RequiredArgsConstructor
public class HotelController extends BaseController {
    private final HotelService hotelService;

    @PostMapping(ApiConstant.CREATE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> createHotel(@RequestBody @Valid HotelRequest request) {
        return created(hotelService.createHotel(request));
    }

    //Anyone can view the hotel they want to reserve
    @GetMapping(ApiConstant.VIEW + ApiConstant.SLASH + "{id}")
    public ResponseEntity<ApiResponse<?>> getHotelById(@PathVariable Integer id) {
        return ok(hotelService.getHotelById(id));
    }
    //Public — anyone can browse all hotels
    @GetMapping(ApiConstant.LIST)
    public ResponseEntity<ApiResponse<?>> getAllHotels(@PageableDefault(page = 0, size = 10, sort = "name") Pageable pageable) {
        return ok(hotelService.getAllHotels(pageable));
    }

    @GetMapping(ApiConstant.SEARCH + ApiConstant.SLASH + ApiConstant.CITY)
    public ResponseEntity<ApiResponse<?>> getHotelsByCity(@RequestParam String city, @PageableDefault(page = 0, size = 10, sort = "name") Pageable pageable) {
        return ok(hotelService.getHotelsByCity(city, pageable));
    }

    @PutMapping(ApiConstant.UPDATE + ApiConstant.SLASH + "{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> updateHotel(@PathVariable Integer id, @RequestBody @Valid HotelRequest request) {
        return ok(hotelService.updateHotel(id, request));
    }

    // Deleting a hotel also deletes all its rooms due to cascade setup
    @DeleteMapping(ApiConstant.DELETE + ApiConstant.SLASH + "{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> deleteHotel(@PathVariable Integer id) {
        return noContent(hotelService.deleteHotel(id));
    }

}
