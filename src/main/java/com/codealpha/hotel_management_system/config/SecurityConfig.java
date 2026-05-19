package com.codealpha.hotel_management_system.config;

import com.codealpha.hotel_management_system.config.jwt.JwtAuthFilter;
import com.codealpha.hotel_management_system.config.jwt.JwtUtils;
import com.codealpha.hotel_management_system.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity//@EnableWebSecurity activates Spring Security for the whole application.
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthFilter jwtAuthFilter;
    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    // AuthenticationProvider Bean
    // This is the component that actually performs authentication.
    // It uses our CustomUserDetailsService to load the user from DB
    // and our PasswordEncoder to verify the password.
    // Spring Security calls this during login automatically.
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // SecurityFilterChain Bean
    // This is where we define the actual security rules —
    // which endpoints are public, which need authentication,
    // which need admin role, and how sessions are managed.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // AUTH endpoints — completely public
                        // Anyone can register or login without a token
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        // HOTEL read endpoints — public
                        // Anyone can browse hotels without logging in
                        .requestMatchers(HttpMethod.GET, "/api/v1/hotels/**").permitAll()
                        // ROOM read endpoints — public
                        // Anyone can view available rooms without logging in
                        .requestMatchers(HttpMethod.GET, "/api/v1/hotels/*/rooms/**").permitAll()
                        // Everything else requires authentication
                        // This covers reservations, payments, admin operations
                        .anyRequest().authenticated()
                )
                // Register our AuthenticationProvider
                // Spring Security uses this to verify credentials during login
                .authenticationProvider(authenticationProvider())
                // Add our JwtAuthFilter BEFORE Spring's default
                // UsernamePasswordAuthenticationFilter.
                // This ensures JWT is checked first on every request
                // before any other authentication mechanism runs.
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}
