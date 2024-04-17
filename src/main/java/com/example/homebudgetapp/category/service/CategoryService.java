package com.example.homebudgetapp.category.service;

import com.example.homebudgetapp.category.dto.CategoryDto;
import com.example.homebudgetapp.category.dto.CategoryResponse;
import com.example.homebudgetapp.category.entity.Category;
import com.example.homebudgetapp.category.repository.CategoryRepository;
import com.example.homebudgetapp.core.exception.HomeBudgetException;
import com.example.homebudgetapp.core.exception.Messages;
import com.example.homebudgetapp.expense.repository.ExpenseRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ExpenseRepository expenseRepository;

    public CategoryResponse insertCategory(CategoryDto categoryDto) throws HomeBudgetException {

        Optional<Category> categoryOptional = categoryRepository.findByName(categoryDto.getName());
        if (categoryOptional.isPresent()) {
            throw new HomeBudgetException(Messages.ENTITY_EXISTS);
        }
        Category category = categoryRepository.saveAndFlush(toEntity(categoryDto));
        return fromEntity(category);
    }

    public CategoryResponse getCategory(Long id) {
        Category category = getCategoryById(id);
        return fromEntity(category);
    }

    public List<CategoryResponse> getCategories() {
        List<Category> categoryList = categoryRepository.findAll();
        return categoryList.stream().map(this::fromEntity).toList();
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Messages.ENTITY_NOT_FOUND.getMessage()));
    }


    @Transactional
    public CategoryResponse updateCategory(Long id, CategoryDto categoryDto) {
        Category category = getCategoryById(id);

        if (Objects.nonNull(categoryDto.getName())) {
            category.setName(categoryDto.getName());
        }

        if (Objects.nonNull(categoryDto.getDescription())) {
            category.setDescription(categoryDto.getDescription());
        }

        categoryRepository.saveAndFlush(category);
        return fromEntity(category);
    }

    public HttpStatus deleteCategory(Long id) {
        expenseRepository.findByCategoryId(id);
        categoryRepository.delete(getCategoryById(id));
        return HttpStatus.NO_CONTENT;
    }

    // --------------------- MAPPER METHODS --------------------- //
    public Category toEntity(CategoryDto categoryDto) {
        Category category = new Category();
        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());
        return category;
    }

    public CategoryResponse fromEntity(Category category) {
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setId(category.getId());
        categoryResponse.setName(category.getName());
        categoryResponse.setDescription(category.getDescription());
        return categoryResponse;
    }
}
