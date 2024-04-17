package com.example.homebudgetapp.expense.dto;

import lombok.Data;
import java.math.BigDecimal;


@Data
public class ExpenseDto {
    private String description;
    private BigDecimal amount;
    private Long categoryId;
}
