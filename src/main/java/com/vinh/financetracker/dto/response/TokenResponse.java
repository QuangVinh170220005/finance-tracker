package com.vinh.financetracker.dto.response;

public record TokenResponse(
        String accessToken,
        String refreshToken,
        String tokenType
) {
}
