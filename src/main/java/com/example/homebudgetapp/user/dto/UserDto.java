package com.example.homebudgetapp.user.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserDto {
    private String username;
    private String password;
    private String email;
}
