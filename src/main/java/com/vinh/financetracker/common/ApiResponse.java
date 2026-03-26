package com.vinh.financetracker.common;

public record ApiResponse<T>(
        boolean success,
        String message,
        T data,
        java.time.LocalDateTime timestamp
) {
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, "Operation successful", data, java.time.LocalDateTime.now());
    }
}
