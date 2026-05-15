package com.codealpha.hotel_management_system.dto.requests;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserUpdateRequest {
    @Size(min = 2, max = 100)
    private String name;
    private String phone;
}
