package com.codealpha.hotel_management_system.service.impl;

import com.codealpha.hotel_management_system.dto.requests.RoomRequest;
import com.codealpha.hotel_management_system.dto.requests.RoomStatusUpdateRequest;
import com.codealpha.hotel_management_system.dto.response.ApiResponse;
import com.codealpha.hotel_management_system.dto.response.PagedResponse;
import com.codealpha.hotel_management_system.dto.response.RoomResponse;
import com.codealpha.hotel_management_system.entity.Hotel;
import com.codealpha.hotel_management_system.entity.Room;
import com.codealpha.hotel_management_system.enums.RoomStatus;
import com.codealpha.hotel_management_system.exception.DuplicateResourceException;
import com.codealpha.hotel_management_system.exception.ResourceNotFoundException;
import com.codealpha.hotel_management_system.exception.RoomNotAvailableException;
import com.codealpha.hotel_management_system.mapper.RoomMapper;
import com.codealpha.hotel_management_system.repository.HotelRepository;
import com.codealpha.hotel_management_system.repository.RoomRepository;
import com.codealpha.hotel_management_system.service.RoomService;
import com.codealpha.hotel_management_system.utils.ResponseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;
    private final HotelRepository hotelRepository;

    @Override
    public ApiResponse<?> addRoom(Integer hotelId, RoomRequest request) {
        log.debug("Adding room to hotel with id: {}", hotelId);
        Hotel hotel = hotelRepository.findById(hotelId).orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + hotelId));
        // Room 101 can exist in hotel 1 AND hotel 2
        // but cannot exist twice in the same hotel
        if (roomRepository.existsByHotelIdAndRoomNumber(hotelId, request.getRoomNumber())) {throw new DuplicateResourceException("Room already exists with number: " + request.getRoomNumber() + " in hotel: " + hotel.getName());
        }
        // Convert RoomRequest → Room entity
        // hotel is passed in because mapper needs it to set the FK
        Room room = roomMapper.toEntity(request, hotel);
        Room saved = roomRepository.save(room);
        log.info("Room added successfully with id: {}", saved.getId());
        return ResponseUtil.getCreatedResponseWithData(roomMapper.toResponse(saved), "Room added successfully");
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<?> getRoomById(Integer hotelId, Integer roomId) {
        log.debug("Fetching room with id: {} from hotel: {}", roomId, hotelId);
        // findByIdAndHotelId ensures room belongs to this hotel
        // returns empty if room exists but belongs to different hotel
        Room room = roomRepository.findByIdAndHotelId(roomId, hotelId).orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + roomId + " in hotel: " + hotelId));
        return ResponseUtil.getSuccessResponseWithData(roomMapper.toResponse(room), "Room fetched successfully");
    }

    // Get all rooms of a specific hotel with pagination
    // First verify hotel exists then fetch its rooms
    @Override
    @Transactional(readOnly = true)
    public ApiResponse<?> getRoomsByHotel(Integer hotelId, Pageable pageable) {
        log.debug("Fetching all rooms for hotel: {}", hotelId);
        // Verify hotel exists first — better error message
        // than returning an empty list silently
        if (!hotelRepository.existsById(hotelId)) {
            throw new ResourceNotFoundException("Hotel not found with id: " + hotelId);
        }
        Page<Room> roomPage = roomRepository.findByHotelId(hotelId, pageable);
        PagedResponse<RoomResponse> response = PagedResponse.fromPage(roomPage, roomMapper::toResponse);
        return ResponseUtil.getSuccessResponseWithData(response, "Rooms fetched successfully");
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<?> getAvailableRooms(Integer hotelId, Pageable pageable) {
        log.debug("Fetching available rooms for hotel: {}", hotelId);
        if (!hotelRepository.existsById(hotelId)) {
            throw new ResourceNotFoundException("Hotel not found with id: " + hotelId);
        }
        // findByHotelIdAndStatus filters by both hotelId AND status
        // only AVAILABLE rooms are returned — OCCUPIED and
        // MAINTENANCE rooms are excluded automatically
        Page<Room> roomPage = roomRepository.findByHotelIdAndStatus(hotelId, RoomStatus.AVAILABLE, pageable);
        PagedResponse<RoomResponse> response = PagedResponse.fromPage(roomPage, roomMapper::toResponse);
        return ResponseUtil.getSuccessResponseWithData(response, "Available rooms fetched successfully");
    }

    @Override
    @Transactional
    public ApiResponse<?> updateRoom(Integer hotelId, Integer roomId, RoomRequest request) {
        log.debug("Updating room with id: {} in hotel: {}", roomId, hotelId);
        // Scope to hotel — room must belong to this hotel
        Room room = roomRepository.findByIdAndHotelId(roomId, hotelId).orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + roomId + " in hotel: " + hotelId));
        // If room number is being changed check it does not already exist in this hotel
        if (request.getRoomNumber() != null && !request.getRoomNumber().equals(room.getRoomNumber()) && roomRepository.existsByHotelIdAndRoomNumber(hotelId, request.getRoomNumber())) {
            throw new DuplicateResourceException("Room already exists with number: " + request.getRoomNumber());
        }
        // Apply only non null fields from request onto existing room
        Room updated = roomMapper.updateEntity(request, room);
        Room saved = roomRepository.save(updated);
        log.info("Room updated successfully with id: {}", saved.getId());
        return ResponseUtil.getSuccessResponseWithData(roomMapper.toResponse(saved), "Room updated successfully");
    }

    @Override
    public ApiResponse<?> updateRoomStatus(Integer hotelId, Integer roomId, RoomStatusUpdateRequest request) {
        log.debug("Updating status of room: {} in hotel: {}", roomId, hotelId);
        Room room = roomRepository.findByIdAndHotelId(roomId, hotelId).orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + roomId + " in hotel: " + hotelId));
        // Cannot change status of a room that has active reservations
        // If room is OCCUPIED it means a guest is currently staying
        // admin cannot put it in MAINTENANCE while occupied
        if (room.getStatus() == RoomStatus.OCCUPIED && request.getStatus() == RoomStatus.MAINTENANCE) {
            throw new RoomNotAvailableException("Cannot set room " + room.getRoomNumber() + " to MAINTENANCE while it is OCCUPIED");
        }
        // Apply status change via mapper
        Room updated = roomMapper.updateStatus(request, room);
        Room saved = roomRepository.save(updated);
        log.info("Room status updated to: {} for room: {}", saved.getStatus(), roomId);
        return ResponseUtil.getSuccessResponseWithData(roomMapper.toResponse(saved), "Room status updated to " + saved.getStatus());
    }

    @Override
    public ApiResponse<?> deleteRoom(Integer hotelId, Integer roomId) {
        log.debug("Deleting room with id: {} from hotel: {}", roomId, hotelId);
        Room room = roomRepository.findByIdAndHotelId(roomId, hotelId).orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + roomId + " in hotel: " + hotelId));
        // Prevent deleting a room that currently has a guest in it
        if (room.getStatus() == RoomStatus.OCCUPIED) {
            throw new RoomNotAvailableException("Cannot delete room " + room.getRoomNumber() + " while it is OCCUPIED");
        }
        roomRepository.delete(room);
        log.info("Room deleted successfully with id: {}", roomId);
        return ResponseUtil.getSuccessResponse("Room deleted successfully");
    }
}
