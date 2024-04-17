package com.example.homebudgetapp.expense.dto;

import com.example.homebudgetapp.category.dto.CategoryResponse;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ExpenseResponse {
    private Long id;
    private String description;
    private BigDecimal amount;
    private LocalDateTime createdAt;
    private String username;
    private CategoryResponse categoryResponse;
}
