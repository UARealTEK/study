package org.example.study.DTOs;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record BorrowRecordRequestDto(
        @NotNull
        @Min(0)
        Long userId,
        @NotNull
        @Min(0)
        Long bookId
)
{}
