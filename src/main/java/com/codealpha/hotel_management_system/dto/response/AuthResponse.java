package com.codealpha.hotel_management_system.dto.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String token;       // JWT bearer token
    private String tokenType;   // always "Bearer" per OAuth convention for security
    private String email;
    private String name;
    private String role;
}
