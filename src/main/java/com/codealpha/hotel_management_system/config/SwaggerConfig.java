package com.codealpha.hotel_management_system.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info().title("StayEase — Hotel Reservation System").description("REST API for managing hotel reservations. " + "Built with Spring Boot for CodeAlpha Internship.").version("1.0.0")
                        .contact(new Contact().name("Swochhandita Ghimire")))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components().addSecuritySchemes("Bearer Authentication", new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT").description("Enter your JWT token here. " + "Get it from /api/v1/auth/login")));
    }
}
