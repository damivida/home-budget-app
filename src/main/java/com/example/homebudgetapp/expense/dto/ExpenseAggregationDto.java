package com.example.homebudgetapp.expense.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ExpenseAggregationDto {

    private BigDecimal totalIncome;
    private BigDecimal totalExpanse;
    private LocalDateTime dateFrom;
    private LocalDateTime dateTo;
    private List<UserExpenseIncomeDto> userExpenseIncomeDtoList;
}
