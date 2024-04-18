package com.example.homebudgetapp.user.dto;

import com.example.homebudgetapp.user.entity.IncomeType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserIncomeDto {

    @NotNull(message = "Income type cannot be null.")
    private IncomeType incomeType;

    private String description;

    @NotNull(message = "Amount cannot be null.")
    @DecimalMin(value = "0.01", inclusive = true, message = "Minimum amount is 0.01")
    private BigDecimal amount;
}
