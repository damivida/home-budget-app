package com.example.homebudgetapp.user.dto;

import com.example.homebudgetapp.user.entity.IncomeType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserIncomeDto {

    private IncomeType incomeType;
    private String description;
    private BigDecimal amount;
}
