package com.codealpha.hotel_management_system.repository;

import com.codealpha.hotel_management_system.entity.User;
import com.codealpha.hotel_management_system.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    boolean existsByRole(Role role);
}
