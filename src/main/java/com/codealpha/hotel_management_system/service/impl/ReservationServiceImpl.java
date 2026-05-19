package com.codealpha.hotel_management_system.service.impl;

import com.codealpha.hotel_management_system.dto.requests.ReservationRequest;
import com.codealpha.hotel_management_system.dto.requests.ReservationStatusUpdateRequest;
import com.codealpha.hotel_management_system.dto.response.ApiResponse;
import com.codealpha.hotel_management_system.dto.response.PagedResponse;
import com.codealpha.hotel_management_system.dto.response.ReservationResponse;
import com.codealpha.hotel_management_system.entity.Reservation;
import com.codealpha.hotel_management_system.entity.Room;
import com.codealpha.hotel_management_system.entity.User;
import com.codealpha.hotel_management_system.enums.ReservationStatus;
import com.codealpha.hotel_management_system.enums.RoomStatus;
import com.codealpha.hotel_management_system.exception.ApiException;
import com.codealpha.hotel_management_system.exception.ResourceNotFoundException;
import com.codealpha.hotel_management_system.exception.RoomNotAvailableException;
import com.codealpha.hotel_management_system.exception.UnauthorizedException;
import com.codealpha.hotel_management_system.mapper.ReservationMapper;
import com.codealpha.hotel_management_system.repository.ReservationRepository;
import com.codealpha.hotel_management_system.repository.RoomRepository;
import com.codealpha.hotel_management_system.repository.UserRepository;
import com.codealpha.hotel_management_system.service.PdfService;
import com.codealpha.hotel_management_system.service.ReservationService;
import com.codealpha.hotel_management_system.utils.ResponseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final ReservationMapper reservationMapper;
    private final PdfService pdfService;

    // Create a new reservation
    // This is the most complex method — it has multiple
    // validation steps before saving to DB
    @Override
    @Transactional
    public ApiResponse<?> createReservation(ReservationRequest request, String email) {
        log.debug("Creating reservation for user: {}", email);
        // Load the user from DB using email from JWT token We do not trust any userId from the request body
        // we always get the user from the token for security
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        // Step 2: Load the room from DB
        Room room = roomRepository.findById(request.getRoomId()).orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + request.getRoomId()));
        // Step 3: Check if room is under maintenance
        // A room under MAINTENANCE cannot be booked at all
        if (room.getStatus() == RoomStatus.MAINTENANCE) {
            throw new RoomNotAvailableException("Room " + room.getRoomNumber() + " is currently under maintenance");
        }
        // checkOut must be strictly after checkIn
        // Example: checkIn = April 5, checkOut = April 5 is invalid
        // checkIn = April 5, checkOut = April 4 is also invalid
        if (!request.getCheckOutDate().isAfter(request.getCheckInDate())) {
            throw new ApiException("Check-out date must be after check-in date", HttpStatus.BAD_REQUEST);
        }
        // Step 5: Check for overlapping reservations
        // This is the core availability check —
        // we query DB to see if any PENDING or CONFIRMED reservation
        // already exists for this room in the requested date range
        // Overlap condition (covers ALL overlap scenarios):
        // existing.checkIn < requested.checkOut AND existing.checkOut > requested.checkIn
        boolean isOverlapping = reservationRepository.existsOverlappingReservation(Long.valueOf(request.getRoomId()), request.getCheckInDate(), request.getCheckOutDate());
        if (isOverlapping) {
            throw new RoomNotAvailableException("Room " + room.getRoomNumber() + " is not available from " + request.getCheckInDate() + " to " + request.getCheckOutDate());
        }
        // Mapper calculates totalPrice = pricePerNight × numberOfNights and sets status to PENDING automatically
        Reservation reservation = reservationMapper.toEntity(request, user, room);
        Reservation saved = reservationRepository.save(reservation);
        log.info("Reservation created successfully with id: {}", saved.getId());
        return ResponseUtil.getCreatedResponseWithData(reservationMapper.toResponse(saved), "Reservation created successfully");
    }

    // Both admin and guest can call this but with different rules:
    // Admin — can see any reservation
    // Guest — can only see their own reservation  We enforce this by checking ownership for non-admin users
    @Override
    @Transactional(readOnly = true)
    public ApiResponse<?> getReservationById(Integer id, String email) {
        log.debug("Fetching reservation with id: {}", id);
        Reservation reservation = reservationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id: " + id));
        // Ownership check — if the email on the reservation does not
        // match the logged in user's email, they cannot see it
        // Admin bypasses this check in the controller using role check
        if (!reservation.getUser().getEmail().equals(email)) {
            throw new UnauthorizedException("You are not authorized to view this reservation");
        }
        return ResponseUtil.getSuccessResponseWithData(reservationMapper.toResponse(reservation), "Reservation fetched successfully");
    }

    // Get all reservations — admin only
    // Returns every reservation in the system with pagination
    @Override
    @Transactional(readOnly = true)
    public ApiResponse<?> getAllReservations(Pageable pageable) {
        log.debug("Fetching all reservations");
        Page<Reservation> reservationPage = reservationRepository.findAll(pageable);
        PagedResponse<ReservationResponse> response = PagedResponse.fromPage(reservationPage, reservationMapper::toResponse);
        return ResponseUtil.getSuccessResponseWithData(response, "Reservations fetched successfully");
    }

    // Get reservations of the currently logged in guest
    // email from JWT token ensures guests only see their own
    @Override
    @Transactional(readOnly = true)
    public ApiResponse<?> getMyReservations(String email, Pageable pageable) {
        log.debug("Fetching reservations for user: {}", email);
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        Page<Reservation> reservationPage = reservationRepository.findByUserId(user.getId(), pageable);
        PagedResponse<ReservationResponse> response = PagedResponse.fromPage(reservationPage, reservationMapper::toResponse);
        return ResponseUtil.getSuccessResponseWithData(response, "Your reservations fetched successfully");
    }

    // Handles status transitions like:
    // PENDING → CONFIRMED (after payment)
    // CONFIRMED → COMPLETED (after checkout)
    @Override
    @Transactional
    public ApiResponse<?> updateReservationStatus(Integer id, ReservationStatusUpdateRequest request) {
        log.debug("Updating status of reservation: {}", id);
        Reservation reservation = reservationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id: " + id));
        // Cannot update a reservation that is already CANCELLED
        // Once cancelled it is final — must create a new one
        if (reservation.getStatus() == ReservationStatus.CANCELLED) {
            throw new ApiException("Cannot update a cancelled reservation", HttpStatus.BAD_REQUEST);
        }
        // Cannot update a reservation that is already COMPLETED
        // Completed means guest has checked out — it is historical
        if (reservation.getStatus() == ReservationStatus.COMPLETED) {
            throw new ApiException("Cannot update a completed reservation", HttpStatus.BAD_REQUEST);
        }
        // Apply status change via mapper
        Reservation updated = reservationMapper.updateStatus(request, reservation);
        Reservation saved = reservationRepository.save(updated);
        log.info("Reservation status updated to: {} for id: {}", saved.getStatus(), id);
        return ResponseUtil.getSuccessResponseWithData(reservationMapper.toResponse(saved), "Reservation status updated to " + saved.getStatus());
    }

    // We verify ownership before allowing cancellation
    @Override
    @Transactional
    public ApiResponse<?> cancelReservation(Integer id, String email) {
        log.debug("Cancelling reservation with id: {}", id);
        Reservation reservation = reservationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id: " + id));
        // Ownership check — guest can only cancel their own reservation
        if (!reservation.getUser().getEmail().equals(email)) {
            throw new UnauthorizedException("You are not authorized to cancel this reservation");
        }
        // Cannot cancel a reservation that is already CANCELLED
        if (reservation.getStatus() == ReservationStatus.CANCELLED) {
            throw new ApiException("Reservation is already cancelled", HttpStatus.BAD_REQUEST);
        }
        // Cannot cancel a reservation that is already COMPLETED
        // Guest has already checked out — nothing to cancel
        if (reservation.getStatus() == ReservationStatus.COMPLETED) {
            throw new ApiException("Cannot cancel a completed reservation", HttpStatus.BAD_REQUEST);
        }
        reservation.setStatus(ReservationStatus.CANCELLED);
        // Set room back to AVAILABLE since reservation is cancelled
        // The room can now be booked by someone else
        reservation.getRoom().setStatus(RoomStatus.AVAILABLE);
        reservationRepository.save(reservation);
        log.info("Reservation cancelled successfully with id: {}", id);
        return ResponseUtil.getSuccessResponse("Reservation cancelled successfully");
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] exportReservationPdf(Integer reservationId, String email) {
        log.debug("Exporting PDF for reservation id: {}", reservationId);
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id: " + reservationId));
        if (!reservation.getUser().getEmail().equals(email)) {
            throw new UnauthorizedException("You are not authorized to export this reservation");
        }
        ReservationResponse response = reservationMapper.toResponse(reservation);
        return pdfService.generateReservationPdf(response);
    }
}
