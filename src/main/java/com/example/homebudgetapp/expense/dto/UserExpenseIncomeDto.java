package com.example.homebudgetapp.expense.dto;

import com.example.homebudgetapp.user.dto.UserIncomeDto;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserExpenseIncomeDto {

    private Long userId;
    private String userName;
    private BigDecimal expense;
    private BigDecimal income;

}
