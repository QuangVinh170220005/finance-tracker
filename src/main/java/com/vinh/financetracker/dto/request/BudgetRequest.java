package com.vinh.financetracker.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record BudgetRequest(
        @NotNull
        BigDecimal amount,
        @Min(1) @Max(12)
        int month,
        @Min(2026)
        int year,
        @NotNull UUID categoryId
        ) {
}
