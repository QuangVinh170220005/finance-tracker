package com.vinh.financetracker.domain.repository;

import com.vinh.financetracker.domain.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    Page<Transaction> findAllByUserId(UUID userId, Pageable pageable);

    Page<Transaction> findAllByUserIdAndTransactionDateBetween(
            UUID userId,
            LocalDate startDate,
            LocalDate endDate,
            Pageable pageable
    );
    Optional<Transaction> findByIdAndUserId(UUID id, UUID userId);

    Page<Transaction> findAllByUserIdAndCategoryId(UUID userId, UUID categoryId, Pageable pageable);

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
    @Query("""
        SELECT SUM(t.amount) 
        FROM Transaction t 
        WHERE t.user.id = :userId 
          AND t.category.id = :categoryId 
          AND t.category.type = 'EXPENSE'
          AND t.transactionDate >= :startDate 
          AND t.transactionDate <= :endDate """)
    BigDecimal sumAmountByCategoryAndDateRange(
            UUID userId,
            UUID categoryId,
            LocalDate startDate,
            LocalDate endDate
    );
}
