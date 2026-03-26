package com.vinh.financetracker.domain.entity;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "categories")
@Getter @Setter @NoArgsConstructor
public class Category extends BaseEntity {
    @Column (nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column (nullable = false)
    private TransactionType type;

    private String icon;
    private String color;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
