package com.vinh.financetracker.domain.repository;

import com.vinh.financetracker.domain.entity.Category;
import com.vinh.financetracker.domain.entity.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {

    // Lấy tất cả category của một user cụ thể
    List<Category> findAllByUserId(UUID userId);

    // Lấy category theo type (INCOME/EXPENSE) của user
    List<Category> findAllByUserIdAndType(UUID userId, TransactionType type);

    // Tìm category cụ thể của user (đảm bảo tính bảo mật)
    Optional<Category> findByIdAndUserId(UUID id, UUID userId);

    // Kiểm tra tên category đã tồn tại cho user này chưa (để tránh trùng tên)
    boolean existsByUserIdAndName(UUID userId, String name);
}
