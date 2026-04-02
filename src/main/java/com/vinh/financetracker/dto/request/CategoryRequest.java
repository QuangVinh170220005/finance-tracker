package com.vinh.financetracker.dto.request;

import com.vinh.financetracker.domain.entity.Transaction;
import com.vinh.financetracker.domain.entity.TransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CategoryRequest(
        @NotBlank(message = "Category name cannot be empty")
        String name,
        @NotNull(message = "Category type cannot be null")
        TransactionType type,
        String icon,
        String color
) {
}
