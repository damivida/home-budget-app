package com.example.homebudgetapp.expense.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class TotalSpendIncomeDto {

    @JsonProperty(namespace = "totalHouseholdExpense")
    private BigDecimal totalHouseholdExpense;

    @JsonProperty(namespace = "totalHouseholdIncome")
    private BigDecimal totalHouseholdIncome;

    @JsonProperty(namespace = "fromDate")
    private LocalDateTime fromDate;

    @JsonProperty(namespace = "toDate")
    private LocalDateTime toDate;

    @JsonProperty(namespace = "totalExpensesIncomesPerUser")
    private List<UserIncomeExpense> totalExpensesIncomesPerUser;
}

