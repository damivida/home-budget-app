package com.example.homebudgetapp.caregory;


import com.example.homebudgetapp.category.dto.CategoryDto;
import com.example.homebudgetapp.category.dto.CategoryResponse;
import com.example.homebudgetapp.category.entity.Category;
import com.example.homebudgetapp.category.repository.CategoryRepository;
import com.example.homebudgetapp.category.service.CategoryService;
import com.example.homebudgetapp.core.exception.HomeBudgetException;
import com.example.homebudgetapp.expense.repository.ExpenseRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ExpenseRepository expenseRepository;

    @InjectMocks
    private CategoryService categoryService;
    private CategoryDto categoryDto;
    private Category category;

    @BeforeEach
    void setUp() {
        categoryDto = new CategoryDto();
        categoryDto.setName("Utilities");
        categoryDto.setDescription("Monthly bills");

        category = new Category();
        category.setName("Utilities");
        category.setDescription("Monthly bills");
    }

    @Test
    void testInsertCategory_NewCategory() throws HomeBudgetException {
        when(categoryRepository.findByName(anyString())).thenReturn(Optional.empty());
        when(categoryRepository.saveAndFlush(Mockito.any(Category.class))).thenReturn(category);

        CategoryResponse result = categoryService.insertCategory(categoryDto);

        assertNotNull(result);
        assertEquals("Utilities", result.getName());
        verify(categoryRepository).saveAndFlush(Mockito.any(Category.class));
    }

    @Test
    void testInsertCategory_ExistingCategory() {
        when(categoryRepository.findByName(anyString())).thenReturn(Optional.of(category));

        Exception exception = assertThrows(HomeBudgetException.class, () -> {
            categoryService.insertCategory(categoryDto);
        });

        assertEquals("Entity already exists.", exception.getMessage());
    }


    @Test
    void testGetCategory_CategoryExists() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));

        CategoryResponse result = categoryService.getCategory(1L);

        assertNotNull(result);
        assertEquals("Utilities", result.getName());
    }


    @Test
    void testGetCategory_CategoryNotFound() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> categoryService.getCategory(1L));
    }

    @Test
    void testUpdateCategory_WithValidChanges() {
        // Arrange
        CategoryDto updatedCategoryDto = new CategoryDto();
        updatedCategoryDto.setName("Updated Name");
        updatedCategoryDto.setDescription("Updated Description");

        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        when(categoryRepository.saveAndFlush(Mockito.any(Category.class))).thenReturn(category);

        // Act
        CategoryResponse result = categoryService.updateCategory(1L, updatedCategoryDto);

        // Assert
        assertNotNull(result);
        assertEquals("Updated Name", result.getName());
        assertEquals("Updated Description", result.getDescription());
    }

    @Test
    public void testDeleteCategory_Success() {
        // Arrange
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(expenseRepository.findByCategoryId(1L)).thenReturn(Collections.emptyList());

        // Act
        HttpStatus status = categoryService.deleteCategory(1L);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, status);
        verify(categoryRepository).delete(category);
    }


    @Test
    void testDeleteCategory_CategoryNotFound() {
        // Arrange
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> categoryService.deleteCategory(1L));
    }
}
