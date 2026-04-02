package com.vinh.financetracker.domain.repository;

import com.vinh.financetracker.domain.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, UUID> {

    // Tìm budget của 1 category cụ thể trong tháng/năm
    Optional<Budget> findByUserIdAndCategoryIdAndMonthAndYear(
            UUID userId, UUID categoryId, int month, int year
    );

    // Lấy danh sách tất cả budget của user trong 1 tháng
    List<Budget> findByUserIdAndMonthAndYear(UUID userId, int month, int year);

    Optional<Budget> findByIdAndUserId(UUID id, UUID userId);
}