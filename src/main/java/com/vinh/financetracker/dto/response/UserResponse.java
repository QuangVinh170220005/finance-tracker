package com.vinh.financetracker.dto.response;

import com.vinh.financetracker.domain.entity.AuthProvider;

import java.util.UUID;

public record UserResponse(
        UUID id,
        String email,
        String fullName,
        AuthProvider provider
) {
}
