package com.vinh.financetracker.controller;


import com.vinh.financetracker.common.ApiResponse;
import com.vinh.financetracker.dto.request.BudgetRequest;
import com.vinh.financetracker.dto.response.BudgetResponse;
import com.vinh.financetracker.service.BudgetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/budgets")
@RequiredArgsConstructor
public class BudgeController {
    private final BudgetService budgetService;

    @PostMapping
    public ResponseEntity<ApiResponse<BudgetResponse>> create(@Valid @RequestBody BudgetRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(budgetService.createBudget(request)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<BudgetResponse>>> getByMonth(
            @RequestParam int month,
            @RequestParam int year) {
        return ResponseEntity.ok(ApiResponse.success(budgetService.getBudgets(month, year)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        budgetService.deleteBudget(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BudgetResponse>> update(@PathVariable UUID id, @RequestBody BudgetResponse request) {
        BudgetResponse response = budgetService.updateBudget(id, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
