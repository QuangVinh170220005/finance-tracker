package com.vinh.financetracker.domain.entity;

import jakarta.persistence.*;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@MappedSuperclass
@Getter
@Setter
public abstract class BaseEntity {
    @Id
    private UUID id = UUID.randomUUID();
    @Column(name = "create_at", nullable = false, updatable = false)
    private java.time.LocalDateTime createAt;

    @PrePersist
    protected void onCreate(){
        this.createAt = java.time.LocalDateTime.now();
    }
}
