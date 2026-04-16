package com.vinh.financetracker.dto.response;

import com.vinh.financetracker.dto.request.CategoryReport;

import java.math.BigDecimal;
import java.util.List;

public record MonthlyReportResponse(
        BigDecimal totalIncome,
        BigDecimal totalExpense,
        BigDecimal balance,
        List<CategoryReport> categoryBreakdown
) {
}
