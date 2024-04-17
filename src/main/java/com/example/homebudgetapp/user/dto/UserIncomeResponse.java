package com.example.homebudgetapp.user.dto;

import com.example.homebudgetapp.user.entity.IncomeType;


import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class UserIncomeResponse {
    private IncomeType incomeType;
    private String description;
    private BigDecimal amount;
    private String username;
    private LocalDateTime createdAt;
}
