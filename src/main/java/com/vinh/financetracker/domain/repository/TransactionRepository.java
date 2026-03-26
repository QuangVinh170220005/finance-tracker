package com.vinh.financetracker.domain.repository;

import com.vinh.financetracker.domain.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    // Phân trang danh sách giao dịch của user
    Page<Transaction> findAllByUserId(UUID userId, Pageable pageable);

    // Lọc giao dịch theo khoảng ngày (Dùng cho Report)
    Page<Transaction> findAllByUserIdAndTransactionDateBetween(
            UUID userId,
            LocalDate startDate,
            LocalDate endDate,
            Pageable pageable
    );

    // Tìm giao dịch theo Category của user
    Page<Transaction> findAllByUserIdAndCategoryId(UUID userId, UUID categoryId, Pageable pageable);

    // Custom query để tính tổng thu/chi trong tháng (Sẽ dùng cho Report API ở Giai đoạn 8)
    @Query("SELECT SUM(t.amount) FROM Transaction t " +
            "WHERE t.user.id = :userId " +
            "AND t.category.type = :type " +
            "AND t.transactionDate BETWEEN :startDate AND :endDate")
    java.math.BigDecimal sumAmountByTypeAndDate(
            @Param("userId") UUID userId,
            @Param("type") com.vinh.financetracker.domain.entity.TransactionType type,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
