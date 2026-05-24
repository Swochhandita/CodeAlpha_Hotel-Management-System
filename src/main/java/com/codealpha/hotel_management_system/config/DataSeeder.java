package com.codealpha.hotel_management_system.config;

import com.codealpha.hotel_management_system.entity.User;
import com.codealpha.hotel_management_system.enums.Role;
import com.codealpha.hotel_management_system.repository.UserRepository;
import com.codealpha.hotel_management_system.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RestController;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public void run(String... args) throws Exception {
        if (userRepository.existsByRole(Role.ADMIN)) {
            log.info("Admin user already exists — skipping seeder");
            return;
        }
        User admin = User.builder()
                .name("Super Admin")
                .email("admin@stay.com")
                .password(passwordEncoder.encode("admin123"))
                .phone("9800000000")
                .role(Role.ADMIN)
                .build();
        userRepository.save(admin);
        log.info("Default admin created successfully");
        log.info("Email: admin@stayease.com");
        log.info("Password: admin123");
    }
}
