package com.example.homebudgetapp.user.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserResponse {
    private String username;
    private String email;
    private BigDecimal balance;
}
