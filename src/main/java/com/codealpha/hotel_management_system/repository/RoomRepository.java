package com.codealpha.hotel_management_system.repository;

import com.codealpha.hotel_management_system.entity.Room;
import com.codealpha.hotel_management_system.enums.RoomStatus;
import com.codealpha.hotel_management_system.enums.RoomType;
import org.hibernate.query.Page;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {
    boolean existsByRoomNumber(String roomNumber);
    List<Room> findByHotelId(Integer hotelId, Pageable pageable);
    List<Room> findByHotelIdAndStatus(Integer hotelId, RoomStatus status, Pageable pageable);
    List<Room> findByHotelIdAndRoomType(Integer hotelId, RoomType roomType, Pageable pageable);
    Optional<Room> findByIdAndHotelId(Integer id, Integer hotelId);
    boolean existsByHotelIdAndRoomNumber(Integer hotelId, String roomNumber);
    long countByHotelIdAndStatus(Integer hotelId, RoomStatus status);
}
