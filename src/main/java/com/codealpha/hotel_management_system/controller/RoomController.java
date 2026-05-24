package com.codealpha.hotel_management_system.controller;

import com.codealpha.hotel_management_system.constant.ApiConstant;
import com.codealpha.hotel_management_system.core.controller.BaseController;
import com.codealpha.hotel_management_system.dto.requests.RoomRequest;
import com.codealpha.hotel_management_system.dto.requests.RoomStatusUpdateRequest;
import com.codealpha.hotel_management_system.dto.response.ApiResponse;
import com.codealpha.hotel_management_system.service.RoomService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Rooms", description = "Room management endpoints")
@RestController
@RequestMapping(ApiConstant.API + ApiConstant.SLASH + ApiConstant.HOTELS + ApiConstant.SLASH + "{hotelId}" + ApiConstant.SLASH + ApiConstant.ROOMS)
@RequiredArgsConstructor
public class RoomController extends BaseController {
    private final RoomService roomService;

    @PostMapping(ApiConstant.CREATE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> addRoom(@PathVariable Integer hotelId, @RequestBody @Valid RoomRequest request) {
        return created(roomService.addRoom(hotelId, request));
    }

    //Public — view a specific room in a specific hotel
    @GetMapping(ApiConstant.VIEW + ApiConstant.SLASH + "{roomId}")
    public ResponseEntity<ApiResponse<?>> getRoomById(@PathVariable Integer hotelId, @PathVariable Integer roomId) {
        return ok(roomService.getRoomById(hotelId, roomId));
    }

    // Public — list all rooms of a specific hotel with pagination
    @GetMapping(ApiConstant.LIST)
    public ResponseEntity<ApiResponse<?>> getRoomsByHotel(@PathVariable Integer hotelId, @PageableDefault(page = 0, size = 10, sort = "roomNumber") Pageable pageable) {
        return ok(roomService.getRoomsByHotel(hotelId, pageable));
    }

    @GetMapping(ApiConstant.AVAILABLE)
    public ResponseEntity<ApiResponse<?>> getAvailableRooms(@PathVariable Integer hotelId, @PageableDefault(page = 0, size = 10, sort = "roomNumber") Pageable pageable) {
        return ok(roomService.getAvailableRooms(hotelId, pageable));
    }

    @PutMapping(ApiConstant.UPDATE + ApiConstant.SLASH + "{roomId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> updateRoom(@PathVariable Integer hotelId, @PathVariable Integer roomId, @RequestBody @Valid RoomRequest request) {
        return ok(roomService.updateRoom(hotelId, roomId, request));
    }

    //Using patch mapping cause we are just updating single field so no need to pass the whole object
    @PatchMapping(ApiConstant.STATUS + ApiConstant.SLASH + "{roomId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> updateRoomStatus(@PathVariable Integer hotelId, @PathVariable Integer roomId, @RequestBody @Valid RoomStatusUpdateRequest request) {
        return ok(roomService.updateRoomStatus(hotelId, roomId, request));
    }

    @DeleteMapping(ApiConstant.DELETE + ApiConstant.SLASH + "{roomId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> deleteRoom(@PathVariable Integer hotelId, @PathVariable Integer roomId) {
        return noContent(roomService.deleteRoom(hotelId, roomId));
    }
}
