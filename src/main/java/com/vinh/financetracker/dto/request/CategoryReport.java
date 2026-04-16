package com.vinh.financetracker.dto.request;

import java.math.BigDecimal;

public record CategoryReport(
        String categoryName,
        BigDecimal totalAmount,
        double percentage
) {
}
