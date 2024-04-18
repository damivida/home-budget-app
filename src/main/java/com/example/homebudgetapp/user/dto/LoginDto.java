package com.example.homebudgetapp.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginDto {

    @NotBlank(message = "Username cannot be blank")
    private String username;

    @Size(min = 4, max = 16, message = "Password must be between 8 and 16 characters")
    private String password;
}
