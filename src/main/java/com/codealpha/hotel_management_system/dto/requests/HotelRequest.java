package com.codealpha.hotel_management_system.dto.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class HotelRequest {
    @NotBlank(message = "Hotel name is required")
    @Size(max = 200)
    private String name;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "City is required")
    private String city;

    private String country;         // optional; defaults to Nepal in entity

    private String description;

    @NotBlank(message = "Phone number is required")
    private String phone;

    @Email(message = "Invalid email")
    private String email;
}
