package com.vinh.financetracker.service;

import com.vinh.financetracker.domain.entity.TransactionType;
import com.vinh.financetracker.domain.entity.User;
import com.vinh.financetracker.domain.repository.TransactionRepository;
import com.vinh.financetracker.domain.repository.UserRepository;
import com.vinh.financetracker.dto.request.CategoryReport;
import com.vinh.financetracker.dto.response.MonthlyReportResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    private User getCurrentUser(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email).orElseThrow();
    }

    public MonthlyReportResponse getMonthlyReport(int month, int year){
        User currentUser = getCurrentUser();
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.with(TemporalAdjusters.lastDayOfMonth());

        BigDecimal income = transactionRepository.sumAmountByTypeAndDate(currentUser.getId(), TransactionType.INCOME, start, end);
        BigDecimal expense = transactionRepository.sumAmountByTypeAndDate(currentUser.getId(), TransactionType.EXPENSE, start, end);
        BigDecimal totalIncome = income != null ? income : BigDecimal.ZERO;
        BigDecimal totalExpense = expense != null ? expense : BigDecimal.ZERO;

        List<CategoryReport> breakdown = transactionRepository.getExpenseReportByCategory(currentUser.getId(), start, end);
        List<CategoryReport> finalBreakdown = breakdown.stream().map(item -> {
            double percent = totalExpense.compareTo(BigDecimal.ZERO) > 0
                    ? (item.totalAmount().doubleValue() / totalExpense.doubleValue()) * 100
                    : 0;
            return new CategoryReport(item.categoryName(), item.totalAmount(), Math.round(percent * 100.0) / 100.0);
        }).toList();
        return new MonthlyReportResponse(
                totalIncome,
                totalExpense,
                totalIncome.subtract(totalExpense),
                finalBreakdown
        );
    }
}
