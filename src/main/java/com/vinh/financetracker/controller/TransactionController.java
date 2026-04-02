package com.vinh.financetracker.controller;

import com.vinh.financetracker.common.ApiResponse;
import com.vinh.financetracker.dto.request.TransactionRequest;
import com.vinh.financetracker.dto.response.TransactionResponse;
import com.vinh.financetracker.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<ApiResponse<TransactionResponse>> create(@Valid @RequestBody TransactionRequest request) throws BadRequestException {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(transactionService.createTransaction(request)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<TransactionResponse>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(ApiResponse.success(transactionService.getTransactions(page, size)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>>deleteTransaction(@PathVariable UUID id){
        transactionService.deleteTransaction(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TransactionResponse>> updateTransaction(@PathVariable UUID id, @Valid @RequestBody TransactionRequest request) throws BadRequestException {
        return ResponseEntity.ok(ApiResponse.success(transactionService.updateTransaction(id, request)));
    }
}
