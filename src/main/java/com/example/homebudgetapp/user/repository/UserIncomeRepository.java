package com.example.homebudgetapp.user.repository;

import com.example.homebudgetapp.user.entity.UserIncome;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.time.LocalDateTime;
import java.util.List;

public interface UserIncomeRepository extends JpaRepository<UserIncome, Long> {
    @Query("SELECT i FROM UserIncome i WHERE i.createdAt >= :fromDate AND i.createdAt < :toDate")
    List<UserIncome> findTotalIncomeInterval(@Param("fromDate") LocalDateTime fromDate, @Param("toDate") LocalDateTime toDate);
}
