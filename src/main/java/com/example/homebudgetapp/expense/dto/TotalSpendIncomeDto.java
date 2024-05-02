package com.example.homebudgetapp.expense.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class TotalSpendIncomeDto {

    @JsonProperty("totalHouseholdExpense")
    private BigDecimal totalHouseholdExpense;

    @JsonProperty("totalHouseholdIncome")
    private BigDecimal totalHouseholdIncome;

    @JsonProperty("fromDate")
    private LocalDateTime fromDate;

    @JsonProperty("toDate")
    private LocalDateTime toDate;

    @JsonProperty("totalExpensesPerUser")
    private List<TotalExpensePerUser> totalExpensePerUserList;

    @JsonProperty("totalIncomesPerUser")
    private List<TotalIncomePerUser> totalIncomePerUserList;

}

