package com.example.homebudgetapp.user.dto;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class LoginResponse {
    private String username;
    private String message;
    private HttpStatus httpStatus;
}
