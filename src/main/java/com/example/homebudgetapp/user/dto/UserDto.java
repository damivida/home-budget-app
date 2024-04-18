package com.example.homebudgetapp.user.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDto {

    @NotBlank(message = "Username cannot be blank")
    @Size(message = "valid_size", min = 1, max = 255)
    private String username;

    @Size(min = 4, max = 16, message = "Password must be between 8 and 16 characters")
    private String password;

    @Email(message = "Invalid email format.")
    private String email;
}
