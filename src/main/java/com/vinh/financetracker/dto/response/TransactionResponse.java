package com.vinh.financetracker.dto.response;

import java.math.BigDecimal;
import java.time.*;
import java.util.UUID;

public record TransactionResponse(
        UUID id,
        BigDecimal amount,
        String description,
        LocalDate transactionDate,
        CategoryResponse category,
        ZonedDateTime createdAt
) {}
