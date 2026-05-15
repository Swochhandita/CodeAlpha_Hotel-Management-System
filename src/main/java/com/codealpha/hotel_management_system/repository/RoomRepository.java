package com.codealpha.hotel_management_system.repository;

import com.codealpha.hotel_management_system.entity.Room;
import com.codealpha.hotel_management_system.enums.RoomStatus;
import com.codealpha.hotel_management_system.enums.RoomType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {
    boolean existsByRoomNumber(String roomNumber);
    Page<Room> findByHotelId(Integer hotelId, Pageable pageable);
    Page<Room> findByHotelIdAndStatus(Integer hotelId, RoomStatus status, Pageable pageable);
    Page<Room> findByHotelIdAndRoomType(Integer hotelId, RoomType roomType, Pageable pageable);
    Optional<Room> findByIdAndHotelId(Integer id, Integer hotelId);
    boolean existsByHotelIdAndRoomNumber(Integer hotelId, String roomNumber);
    long countByHotelIdAndStatus(Integer hotelId, RoomStatus status);
}
