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

    List<Category> findAllByUserId(UUID userId);

    List<Category> findAllByUserIdAndType(UUID userId, TransactionType type);
    Optional<Category> findByIdAndUserId(UUID id, UUID userId);
    boolean existsByUserIdAndName(UUID userId, String name);
}
