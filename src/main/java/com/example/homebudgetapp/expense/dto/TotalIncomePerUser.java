package com.example.homebudgetapp.expense.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TotalIncomePerUser {
    private String username;
    private BigDecimal amount;
}
