package com.example.homebudgetapp.expense.service;

import com.example.homebudgetapp.category.dto.CategoryResponse;
import com.example.homebudgetapp.category.entity.Category;
import com.example.homebudgetapp.category.service.CategoryService;
import com.example.homebudgetapp.core.configuration.security.SecurityUtils;
import com.example.homebudgetapp.core.exception.HomeBudgetException;
import com.example.homebudgetapp.core.exception.Messages;
import com.example.homebudgetapp.expense.dto.*;
import com.example.homebudgetapp.expense.entity.Expense;
import com.example.homebudgetapp.expense.repository.ExpenseRepository;
import com.example.homebudgetapp.user.entity.User;
import com.example.homebudgetapp.user.entity.UserIncome;
import com.example.homebudgetapp.user.service.UserService;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class ExpenseService {


    private final ExpenseRepository expenseRepository;
    private final CategoryService categoryService;
    private final UserService userService;

    @Transactional
    public HttpStatus insertExpense(ExpenseDto expenseDto) {
        expenseRepository.save(toEntity(expenseDto));

        UserDetails loggedInUser = SecurityUtils.getLoggedInUser();
        User user = userService.findUserByUsername(loggedInUser.getUsername());
        user.setBalance(user.getBalance().subtract(expenseDto.getAmount()));
        userService.saveAndFlushUser(user);
        return HttpStatus.CREATED;
    }

    public ExpenseResponse getExpense(Long id) throws HomeBudgetException {
        return fromEntity(getExpenseById(id));
    }

    public Expense getExpenseById(Long id) throws HomeBudgetException {
        return expenseRepository.findById(id)
                .orElseThrow(() -> new HomeBudgetException(Messages.ENTITY_NOT_FOUND));
    }


    @Transactional
    public ExpenseResponse updateExpense(Long id, ExpenseDto expenseDto) throws HomeBudgetException {
        Expense expense = getExpenseById(id);

        UserDetails loggedInUser = SecurityUtils.getLoggedInUser();
        User user = userService.findUserByUsername(loggedInUser.getUsername());

        if (!Objects.equals(user.getId(), expense.getUser().getId())) {
            throw new HomeBudgetException(Messages.UNAUTHORIZED_ACTION_FOR_EXPENSE);
        }

        if (Objects.nonNull(expenseDto.getAmount())) {

            BigDecimal balanceCorrected = (user.getBalance().add(expense.getAmount())).subtract(expenseDto.getAmount());
            user.setBalance(balanceCorrected);
            userService.saveAndFlushUser(user);

            expense.setAmount(expenseDto.getAmount());
        }

        if (Objects.nonNull(expenseDto.getDescription())) {
            expense.setDescription(expenseDto.getDescription());
        }
        if (Objects.nonNull(expenseDto.getCategoryId())) {
            expense.setCategory(categoryService.getCategoryById(expenseDto.getCategoryId()));
        }

        expenseRepository.saveAndFlush(expense);
        return fromEntity(expense);
    }

    @Transactional
    public ExpenseResponse deleteExpense(Long id) throws HomeBudgetException {

        Expense expense = getExpenseById(id);
        UserDetails loggedInUser = SecurityUtils.getLoggedInUser();
        User user = userService.findUserByUsername(loggedInUser.getUsername());

        if (!Objects.equals(user.getId(), expense.getUser().getId())) {
            throw new HomeBudgetException(Messages.UNAUTHORIZED_ACTION_FOR_EXPENSE);
        }

        user.setBalance(user.getBalance().add(expense.getAmount()));
        userService.saveAndFlushUser(user);
        expenseRepository.delete(expense);

        return fromEntity(expense);
    }

    public List<ExpenseResponse> getExpenses(String description,
                                             String category,
                                             String username,
                                             Long minAmount,
                                             Long maxAmount,
                                             LocalDateTime fromDate,
                                             LocalDateTime toDate) {

        List<Expense> expenseList = expenseRepository.findAll(this.getSpecification(description, category, username, minAmount, maxAmount, fromDate, toDate));
        return expenseList.stream().map(this::fromEntity).toList();
    }

    public TotalSpendIncomeDto getTotalExpenseIncomeAmount(TimeFrame timeFrame, LocalDateTime fromDate, LocalDateTime toDate) throws HomeBudgetException {

        if (Objects.nonNull(timeFrame)) {
            switch (timeFrame) {
                case YESTERDAY:
                    fromDate = LocalDate.now().minusDays(1).atStartOfDay();
                    toDate = LocalDate.now().atStartOfDay();
                    break;
                case CURRENT_WEEK:
                    LocalDate now = LocalDate.now();
                    fromDate = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).atStartOfDay();
                    toDate = now.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).plusDays(1).atStartOfDay();
                    break;
                case PREVIOUS_WEEK:
                    fromDate = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).minusWeeks(1).atStartOfDay();
                    toDate = LocalDate.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).plusDays(1).minusWeeks(1).atStartOfDay();
                    break;
                case CURRENT_MONTH:
                    toDate = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()).atStartOfDay();
                    fromDate = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay();
                    break;
                case PREVIOUS_MONTH:
                    LocalDate firstDayOfPreviousMonth = LocalDate.now().withDayOfMonth(1).minusMonths(1);
                    LocalDate lastDayOfPreviousMonth = LocalDate.now().withDayOfMonth(1).minusDays(1);
                    toDate = lastDayOfPreviousMonth.atStartOfDay();
                    fromDate = firstDayOfPreviousMonth.atStartOfDay();
                    break;
                case CURRENT_YEAR:
                    toDate = LocalDate.now().with(TemporalAdjusters.lastDayOfYear()).atStartOfDay();
                    fromDate = LocalDate.now().with(TemporalAdjusters.firstDayOfYear()).atStartOfDay();
                    break;
                case PREVIOUS_YEAR:
                    toDate = LocalDate.now().with(TemporalAdjusters.lastDayOfYear()).minusYears(1).atStartOfDay();
                    fromDate = LocalDate.now().with(TemporalAdjusters.firstDayOfYear()).minusYears(1).atStartOfDay();
                    break;
            }

            return expenseIncomeAggregator(fromDate, toDate);

        } else {
            if (Objects.isNull(fromDate) || Objects.isNull(toDate)) {
                throw new HomeBudgetException(Messages.INTERVAL_NOT_SPECIFIED);
            }
            if (fromDate.isAfter(toDate)) {
                throw new HomeBudgetException(Messages.INTERVAL_INVALID);
            }

            return expenseIncomeAggregator(fromDate, toDate);
        }
    }


    private Specification getSpecification(String description,
                                           String category,
                                           String username,
                                           Long minAmount,
                                           Long maxAmount,
                                           LocalDateTime fromDate,
                                           LocalDateTime toDate) {
        return (root, query, criteriaBuilder) -> {
            final List<Predicate> searchCriteriaList = new ArrayList<>();

            if (Objects.nonNull(description)) {
                searchCriteriaList.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("description")),
                        "%" + description.toLowerCase() + "%"));
            }

            if (Objects.nonNull(category)) {
                searchCriteriaList.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("category").<String>get("name")), category.toLowerCase() + "%"));
            }

            if (Objects.nonNull(username)) {
                searchCriteriaList.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("user").<String>get("username")), username.toLowerCase() + "%"));
            }

            if (Objects.nonNull(minAmount)) {
                searchCriteriaList.add(criteriaBuilder.greaterThanOrEqualTo(root.get("amount"), minAmount));
            }

            if (Objects.nonNull(maxAmount)) {
                searchCriteriaList.add(criteriaBuilder.lessThanOrEqualTo(root.get("amount"), maxAmount));
            }

            if (Objects.nonNull(fromDate)) {
                searchCriteriaList.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), fromDate));
            }

            if (Objects.nonNull(toDate)) {
                searchCriteriaList.add(criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), toDate));
            }

            return criteriaBuilder.and(searchCriteriaList.toArray(new Predicate[]{}));
        };
    }
    

    private Expense toEntity(ExpenseDto expenseDto) {
        Category category = categoryService.getCategoryById(expenseDto.getCategoryId());

        UserDetails userDetails = SecurityUtils.getLoggedInUser();

        User user = userService.findUserByUsername(userDetails.getUsername());

        Expense expense = new Expense();
        expense.setDescription(expenseDto.getDescription());
        expense.setAmount(expenseDto.getAmount());
        expense.setCategory(category);
        expense.setUser(user);

        return expense;
    }

    private ExpenseResponse fromEntity(Expense expense) {
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setId(expense.getCategory().getId());
        categoryResponse.setName(expense.getCategory().getName());
        categoryResponse.setDescription(expense.getCategory().getDescription());

        ExpenseResponse expenseResponse = new ExpenseResponse();
        expenseResponse.setId(expense.getId());
        expenseResponse.setDescription(expense.getDescription());
        expenseResponse.setAmount(expense.getAmount());
        expenseResponse.setCreatedAt(expense.getCreatedAt());
        expenseResponse.setUsername(expense.getUser().getUsername());
        expenseResponse.setCategoryResponse(categoryResponse);
        return expenseResponse;
    }

   

    private TotalSpendIncomeDto expenseIncomeAggregator(LocalDateTime fromDate, LocalDateTime toDate) {

        // Final object list
        List<UserIncomeExpense> userIncomeExpenseList = new ArrayList<>();

        //  Expenses
        List<Expense> totalExpensesInterval =  expenseRepository.findTotalExpensesInterval(fromDate, toDate);

        // Group expenses by user
        Map<User, List<Expense>> expensesByUser = totalExpensesInterval.stream()
                .collect(Collectors.groupingBy(Expense::getUser));

        for (Map.Entry<User, List<Expense>> entry : expensesByUser.entrySet()) {
            User user = entry.getKey();
            List<Expense> expenses = entry.getValue();

            BigDecimal totalAmount = expenses.stream()
                    .map(Expense::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            UserIncomeExpense userIncomeExpense = new UserIncomeExpense();
            userIncomeExpense.setUsername(user.getUsername());
            userIncomeExpense.setAmount(totalAmount);
            userIncomeExpense.setDescription("Expense");
            userIncomeExpenseList.add(userIncomeExpense);
        }

        // Incomes
        List<UserIncome> userIncomesInterval = userService.userIncomeResponseInterval(fromDate, toDate);
        Map<User, List<UserIncome>> incomeByUser = userIncomesInterval.stream().collect(Collectors.groupingBy(UserIncome::getUser));

        // Group incomes by user
        for (Map.Entry<User, List<UserIncome>> entry : incomeByUser.entrySet()) {
            User user = entry.getKey();
            List<UserIncome> userIncomeList = entry.getValue();

            BigDecimal totalAmount = userIncomeList.stream()
                    .map(UserIncome::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            UserIncomeExpense userIncomeExpense = new UserIncomeExpense();
            userIncomeExpense.setUsername(user.getUsername());
            userIncomeExpense.setAmount(totalAmount);
            userIncomeExpense.setDescription("Income");
            userIncomeExpenseList.add(userIncomeExpense);
        }

        // Calculate total amount of all expenses
        BigDecimal totalAmountSpend = totalExpensesInterval.stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Calculate total amount for all incomes
        BigDecimal totalIncome = userIncomesInterval.stream()
                .map(UserIncome::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Create final response
        TotalSpendIncomeDto totalSpendIncomeDto = new TotalSpendIncomeDto();
        totalSpendIncomeDto.setTotalHouseholdExpense(totalAmountSpend);
        totalSpendIncomeDto.setTotalHouseholdIncome(totalIncome);
        totalSpendIncomeDto.setFromDate(fromDate);
        totalSpendIncomeDto.setToDate(toDate);
        totalSpendIncomeDto.setTotalExpensesIncomesPerUser(userIncomeExpenseList);
        return totalSpendIncomeDto;
    }
}
