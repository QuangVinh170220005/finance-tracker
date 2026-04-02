package com.vinh.financetracker.service;

import com.vinh.financetracker.domain.entity.Category;
import com.vinh.financetracker.domain.entity.Transaction;
import com.vinh.financetracker.domain.entity.User;
import com.vinh.financetracker.domain.repository.CategoryRepository;
import com.vinh.financetracker.domain.repository.TransactionRepository;
import com.vinh.financetracker.domain.repository.UserRepository;
import com.vinh.financetracker.dto.request.TransactionRequest;
import com.vinh.financetracker.dto.response.CategoryResponse;
import com.vinh.financetracker.dto.response.TransactionResponse;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public TransactionResponse createTransaction(TransactionRequest request) throws BadRequestException {
        User currentUser = getCurrentUser();

        Category category = categoryRepository.findByIdAndUserId(request.categoryId(), currentUser.getId())
                .orElseThrow(() -> new BadRequestException("The category is invalid or does not belong to you"));

        Transaction transaction = new Transaction();
        transaction.setAmount(request.amount());
        transaction.setDescription(request.description());
        transaction.setTransactionDate(request.transactionDate());
        transaction.setCategory(category);
        transaction.setUser(currentUser);

        return mapToResponse(transactionRepository.save(transaction));
    }

    public Page<TransactionResponse> getTransactions(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("transactionDate").descending());
        return transactionRepository.findAllByUserId(getCurrentUser().getId(), pageable)
                .map(this::mapToResponse);
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email).get();
    }

    private TransactionResponse mapToResponse(Transaction t) {
        return new TransactionResponse(
                t.getId(), t.getAmount(), t.getDescription(),
                t.getTransactionDate(),
                new CategoryResponse(t.getCategory().getId(), t.getCategory().getName(), t.getCategory().getType(), null, null),
                t.getCreatedAt()
        );
    }
    public TransactionResponse updateTransaction(UUID id, TransactionRequest request) throws BadRequestException {
        User currentUser = getCurrentUser();

        // 1. Tìm giao dịch và kiểm tra quyền sở hữu (Bảo mật IDOR)
        Transaction transaction = transactionRepository.findByIdAndUserId(id, currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy giao dịch này"));

        // 2. Nếu đổi danh mục, phải kiểm tra danh mục mới có hợp lệ không
        if (!transaction.getCategory().getId().equals(request.categoryId())) {
            Category newCategory = categoryRepository.findByIdAndUserId(request.categoryId(), currentUser.getId())
                    .orElseThrow(() -> new BadRequestException("Danh mục mới không hợp lệ"));
            transaction.setCategory(newCategory);
        }

        // 3. Cập nhật các thông tin khác
        transaction.setAmount(request.amount());
        transaction.setDescription(request.description());
        transaction.setTransactionDate(request.transactionDate());

        return mapToResponse(transactionRepository.save(transaction));
    }

    public void deleteTransaction(UUID id) {
        User currentUser = getCurrentUser();

        // Phải kiểm tra đúng ID và đúng User mới cho xóa
        Transaction transaction = transactionRepository.findByIdAndUserId(id, currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy giao dịch để xóa"));

        transactionRepository.delete(transaction);
    }
}
