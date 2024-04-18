package com.example.homebudgetapp.expense.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserIncomeExpense {
    private String username;
    private BigDecimal amount;
    private String description;
}
