package com.example.homebudgetapp.category.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryDto {

    @NotBlank(message = "Category name cannot be blank")
    private String name;
    private String description;
}
