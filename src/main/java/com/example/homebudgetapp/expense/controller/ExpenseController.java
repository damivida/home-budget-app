package com.example.homebudgetapp.expense.controller;

import com.example.homebudgetapp.core.configuration.openapi.OpenApiTags;
import com.example.homebudgetapp.core.exception.HomeBudgetException;
import com.example.homebudgetapp.expense.dto.ExpenseDto;
import com.example.homebudgetapp.expense.dto.ExpenseResponse;
import com.example.homebudgetapp.expense.dto.TimeFrame;
import com.example.homebudgetapp.expense.dto.TotalSpendIncomeDto;
import com.example.homebudgetapp.expense.service.ExpenseService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/home-budget-app/expense")
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping("")
    @Operation(tags = OpenApiTags.EXPENSE, summary = "Create expense.")
    public ResponseEntity<HttpStatus> insertExpense(@RequestBody ExpenseDto expenseDto) {
        return new ResponseEntity<>(expenseService.insertExpense(expenseDto), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(tags = OpenApiTags.EXPENSE, summary = "Get expense by id.")
    public ResponseEntity<ExpenseResponse> getExpense(final @PathVariable("id") Long id) throws HomeBudgetException {
        return new ResponseEntity<>(expenseService.getExpense(id), HttpStatus.OK);
    }

    @GetMapping("")
    @Operation(tags = OpenApiTags.EXPENSE, summary = "Get expenses by filter parameter.")
    public ResponseEntity<List<ExpenseResponse>> getExpenses(
            final @RequestParam(name = "description", required = false) String description,
            final @RequestParam(name = "category", required = false) String category,
            final @RequestParam(name = "username", required = false) String username,
            final @RequestParam(name = "minAmount", required = false) Long minAmount,
            final @RequestParam(name = "maxAmount", required = false) Long maxAmount,
            @RequestParam(name = "fromDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,
            @RequestParam(name = "toDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate
            )  {
        return new ResponseEntity<>(expenseService.getExpenses(description, category, username, minAmount, maxAmount, fromDate, toDate), HttpStatus.OK);
    }


    @GetMapping("/total-spend-income")
    @Operation(tags = OpenApiTags.EXPENSE, summary = "Get total expenses an income amount for selected time frame or wanted interval.")
    public ResponseEntity<TotalSpendIncomeDto> getTotalExpenseAmount(
            @RequestParam(name = "interval", required = false) TimeFrame timeFrame,
            @RequestParam(name = "fromDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,
            @RequestParam(name = "toDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate) throws HomeBudgetException {
        return new ResponseEntity<>(expenseService.getTotalExpenseAmount(timeFrame, fromDate, toDate), HttpStatus.OK);
    }


    @PutMapping("/{id}")
    @Operation(tags = OpenApiTags.EXPENSE, summary = "Update expense.")
    public ResponseEntity<ExpenseResponse> updateExpense(final @PathVariable("id") Long id, @RequestBody ExpenseDto expenseDto) throws HomeBudgetException {
        return new ResponseEntity<>(expenseService.updateExpense(id, expenseDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(tags = OpenApiTags.EXPENSE, summary = "Delete expense.")
    public ResponseEntity<ExpenseResponse> deleteExpense(@NonNull @PathVariable Long id) throws HomeBudgetException {
        return new ResponseEntity<>(expenseService.deleteExpense(id), HttpStatus.OK);
    }
}
