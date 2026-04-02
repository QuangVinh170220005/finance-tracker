package com.vinh.financetracker.dto.response;

import com.vinh.financetracker.domain.entity.Category;

import java.math.BigDecimal;
import java.util.UUID;

public record BudgetResponse(
        UUID id,
        BigDecimal amount,
        BigDecimal actualSpent,
        BigDecimal remaining,
        int month,
        int year,
        CategoryResponse category
) {
}
