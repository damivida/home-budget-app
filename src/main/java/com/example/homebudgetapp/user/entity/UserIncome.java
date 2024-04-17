package com.example.homebudgetapp.user.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "users_income")
public class UserIncome {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "income_type")
    @Enumerated(EnumType.STRING)
    private IncomeType incomeType;

    @Column(name = "description")
    private String description;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
