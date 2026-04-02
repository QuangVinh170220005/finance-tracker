package com.vinh.financetracker.dto.response;

import com.vinh.financetracker.domain.entity.TransactionType;
import java.util.UUID;

public record CategoryResponse(
        UUID id,
        String name,
        TransactionType type,
        String icon,
        String color
) {
}
