package com.vinh.financetracker.common;

public record ValidationErrorResponse(
        String field,
        String message
) {}
