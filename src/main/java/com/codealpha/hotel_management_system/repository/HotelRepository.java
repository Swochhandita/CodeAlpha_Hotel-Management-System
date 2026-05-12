package com.codealpha.hotel_management_system.repository;

import com.codealpha.hotel_management_system.entity.Hotel;
import jdk.dynalink.linker.LinkerServices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Integer> {
        List<Hotel> findByCityContainingIgnoreCase(String city);
        boolean existsByNameIgnoreCaseAndCityIgnoreCase(String name, String city);
}
