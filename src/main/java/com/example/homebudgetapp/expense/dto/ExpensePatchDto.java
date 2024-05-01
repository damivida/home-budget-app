package com.example.homebudgetapp.expense.dto;

import jakarta.validation.constraints.DecimalMin;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ExpensePatchDto {

    private String description;

    @DecimalMin(value = "0.01", inclusive = true, message = "Minimum amount is 0.01")
    private BigDecimal amount;

    private Long categoryId;

    private LocalDateTime createdAt;
}
