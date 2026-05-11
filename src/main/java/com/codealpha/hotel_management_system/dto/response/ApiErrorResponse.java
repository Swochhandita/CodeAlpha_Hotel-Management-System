package com.codealpha.hotel_management_system.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)// if there is non-validation error then it will not show or in JSON that field won't appear.
//Used to show the errors in postman or in swagger
public class ApiErrorResponse {
    private LocalDateTime timestamp;
    private int status;
    private String error;      // human-readable message e.g. "Hotel not found with id : 5"
    private String path;       // the request path that caused the error e.g. "/api/v1/hotels/99"
    /*
     * fieldErrors: only present on validation failures (400).
     * e.g. {"email": "Invalid email format", "name": "Name is required"}
     */
    private Map<String, String> fieldErrors;
}

