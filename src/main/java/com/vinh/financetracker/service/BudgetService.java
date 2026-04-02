package com.vinh.financetracker.service;

import com.vinh.financetracker.domain.entity.Budget;
import com.vinh.financetracker.domain.entity.Category;
import com.vinh.financetracker.domain.entity.User;
import com.vinh.financetracker.domain.repository.BudgetRepository;
import com.vinh.financetracker.domain.repository.CategoryRepository;
import com.vinh.financetracker.domain.repository.TransactionRepository;
import com.vinh.financetracker.domain.repository.UserRepository;
import com.vinh.financetracker.dto.request.BudgetRequest;
import com.vinh.financetracker.dto.response.BudgetResponse;
import com.vinh.financetracker.dto.response.CategoryResponse;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BudgetService {
    private final BudgetRepository budgetRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final TransactionRepository transactionRepository;

    private User getCurrentUser() {
        String email = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email).orElseThrow();
    }
    public BudgetResponse createBudget(BudgetRequest request) {
        User currentUser = getCurrentUser();
        budgetRepository.findByUserIdAndCategoryIdAndMonthAndYear(
                        currentUser.getId(), request.categoryId(), request.month(), request.year())
                .ifPresent(b -> {
                    try {
                        throw new BadRequestException("A buget for this category and month already exists");
                    } catch (BadRequestException e) {
                        throw new RuntimeException(e);
                    }
                });

        Category category = categoryRepository.findByIdAndUserId(request.categoryId(), currentUser.getId())
                .orElseThrow(() -> new IllegalArgumentException("The category is invalid or does not belong to you"));

        Budget budget = new Budget();
        budget.setAmount(request.amount());
        budget.setMonth(request.month());
        budget.setYear(request.year());
        budget.setCategory(category);
        budget.setUser(currentUser);
        return mapToResponse(budgetRepository.save(budget));
    }

    private BudgetResponse mapToResponse(Budget budget) {
        LocalDate start = LocalDate.of(budget.getYear(), budget.getMonth(), 1);
        LocalDate end = start.with(TemporalAdjusters.lastDayOfMonth());

            BigDecimal actualSpent = transactionRepository.sumAmountByCategoryAndDateRange(
                budget.getUser().getId(), budget.getCategory().getId(), start, end);

        if (actualSpent == null) actualSpent = BigDecimal.ZERO;

        return new BudgetResponse(
                budget.getId(),
                budget.getAmount(),
                actualSpent,
                budget.getAmount().subtract(actualSpent),
                budget.getMonth(),
                budget.getYear(),
                new CategoryResponse(budget.getCategory().getId(), budget.getCategory().getName(), budget.getCategory().getType(), null, null)
        );
    }
    public List<BudgetResponse> getBudgets(int month, int year) {
        User currentUser = getCurrentUser();
        return budgetRepository.findByUserIdAndMonthAndYear(currentUser.getId(), month, year)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }
    public void deleteBudget(UUID id) {
        User currentUser = getCurrentUser();
        Budget budget = budgetRepository.findByIdAndUserId(id, currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Budget not found"));
        budgetRepository.delete(budget);
    }

    public BudgetResponse updateBudget(UUID id, BudgetResponse request) {
        User currentUser = getCurrentUser();
        Budget budget = budgetRepository.findByIdAndUserId(id, currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Budget not found"));

        if (!budget.getCategory().getId().equals(request.category().id())
                || budget.getMonth() != request.month()
                || budget.getYear() != request.year()) {
            budgetRepository.findByUserIdAndCategoryIdAndMonthAndYear(
                            currentUser.getId(), request.category().id(), request.month(), request.year())
                    .ifPresent(b -> {
                        try {
                            throw new BadRequestException("A buget for this category and month already exists");
                        } catch (BadRequestException e) {
                            throw new RuntimeException(e);
                        }
                    });
        }

        Category category = categoryRepository.findByIdAndUserId(request.category().id(), currentUser.getId())
                .orElseThrow(() -> new IllegalArgumentException("The category is invalid or does not belong to you"));

        budget.setAmount(request.amount());
        budget.setMonth(request.month());
        budget.setYear(request.year());
        budget.setCategory(category);
        return mapToResponse(budgetRepository.save(budget));
    }
}
