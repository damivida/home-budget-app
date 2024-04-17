package com.example.homebudgetapp.user.repository;

import com.example.homebudgetapp.user.entity.UserIncome;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface UserIncomeRepository extends JpaRepository<UserIncome, Long> {

    @Query("SELECT SUM(i.amount) FROM UserIncome i WHERE i.createdAt >= :fromDate AND i.createdAt < :toDate")
    BigDecimal findTotalIncomeBetweenDates(@Param("fromDate") LocalDateTime fromDate, @Param("toDate") LocalDateTime toDate);

    @Query("SELECT i FROM UserIncome i WHERE i.user.username = :username")
    List<UserIncome> findByUsername(@Param("username") String username);
}
