package com.example.homebudgetapp.category.controller;

import com.example.homebudgetapp.category.dto.CategoryDto;
import com.example.homebudgetapp.category.dto.CategoryResponse;
import com.example.homebudgetapp.category.service.CategoryService;
import com.example.homebudgetapp.core.configuration.openapi.OpenApiTags;
import com.example.homebudgetapp.core.exception.HomeBudgetException;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/home-budget-app/category")
public class CategoryController {

     private final CategoryService categoryService;

    @PostMapping("")
    @Operation(tags = OpenApiTags.CATEGORY, summary = "Create category.")
    public ResponseEntity<CategoryResponse> insertCategory(@RequestBody @Valid CategoryDto categoryDto) throws HomeBudgetException {
        return new ResponseEntity<>(categoryService.insertCategory(categoryDto), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(tags = OpenApiTags.CATEGORY, summary = "Get category by id.")
    public ResponseEntity<CategoryResponse> getCategory(final @PathVariable("id") Long id) {
        return new ResponseEntity<>(categoryService.getCategory(id), HttpStatus.OK);
    }

    @GetMapping("")
    @Operation(tags = OpenApiTags.CATEGORY, summary = "Get categories.")
    public ResponseEntity<List<CategoryResponse>> getCategories() {
        return new ResponseEntity<>(categoryService.getCategories(), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @Operation(tags = OpenApiTags.CATEGORY, summary = "Update category.")
    public ResponseEntity<CategoryResponse> updateCategory(final @PathVariable("id") Long id, @RequestBody CategoryDto categoryDto) throws Exception {
        return new ResponseEntity<>(categoryService.updateCategory(id, categoryDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(tags = OpenApiTags.CATEGORY, summary = "Delete category.")
    public ResponseEntity<HttpStatus> deleteCategory(@NonNull @PathVariable Long id) {
        return new ResponseEntity<>(categoryService.deleteCategory(id), HttpStatus.OK);
    }
}
