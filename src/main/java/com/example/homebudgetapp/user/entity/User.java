package com.example.homebudgetapp.user.entity;

import jakarta.persistence.*;
import lombok.Data;


import java.math.BigDecimal;



@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "balance")
    private BigDecimal balance = BigDecimal.valueOf(0);

}
