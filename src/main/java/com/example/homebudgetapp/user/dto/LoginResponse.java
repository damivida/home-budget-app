package com.example.homebudgetapp.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.http.HttpStatus;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class LoginResponse {
    private String username;
    private String message;
    private String jwtToken;
    private HttpStatus httpStatus;
}
