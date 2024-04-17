package com.example.homebudgetapp.expense.repository;

import com.example.homebudgetapp.expense.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long>, JpaSpecificationExecutor {


    @Query("SELECT SUM(e.amount) FROM Expense e WHERE e.createdAt >= :fromDate AND e.createdAt < :toDate")
    BigDecimal findTotalExpensesBetweenDates(@Param("fromDate") LocalDateTime fromDate, @Param("toDate") LocalDateTime toDate);

    List<Expense> findByCategoryId(Long categoryId);
}
