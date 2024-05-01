package com.example.homebudgetapp.expense.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
public class ExpenseDto {

    private String description;

    @DecimalMin(value = "0.01",  message = "Minimum amount is 0.01")
    private BigDecimal amount;

    @NotNull(message = "Category ID must not be null")
    private Long categoryId;

    @NotNull(message = "Created time must not be null")
    private LocalDateTime createdAt;
}
