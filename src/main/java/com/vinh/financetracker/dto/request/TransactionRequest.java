package com.vinh.financetracker.dto.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record TransactionRequest(
        @NotNull(message = "Amount is required")
        @Positive(message = "Amount must be greater than 0")
        BigDecimal amount,

        String description,

        @NotNull(message = "Transaction date is required")
        LocalDate transactionDate,

        @NotNull(message = "Category is required")
        UUID categoryId
) {}
