package com.example.homebudgetapp.expense.entity;


import com.example.homebudgetapp.category.entity.Category;
import com.example.homebudgetapp.user.entity.User;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "expenses")
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
