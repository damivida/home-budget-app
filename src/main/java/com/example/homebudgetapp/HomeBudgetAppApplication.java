package com.example.homebudgetapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@SpringBootApplication
public class HomeBudgetAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomeBudgetAppApplication.class, args);
	}

}
