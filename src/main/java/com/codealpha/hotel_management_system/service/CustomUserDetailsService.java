package com.codealpha.hotel_management_system.service;

import com.codealpha.hotel_management_system.config.jwt.CustomUserDetails;
import com.codealpha.hotel_management_system.entity.User;
import com.codealpha.hotel_management_system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.debug("Loading user by email: {}", email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {log.warn("User not found with email: {}", email);
                    return new UsernameNotFoundException("User not found with email: " + email);});
        return new CustomUserDetails(user);
    }

}
