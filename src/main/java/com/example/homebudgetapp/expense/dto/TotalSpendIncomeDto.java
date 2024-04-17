package com.example.homebudgetapp.expense.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class TotalSpendIncomeDto {
    private BigDecimal totalSpend;
    private BigDecimal totalIncome;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
}

