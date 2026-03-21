package org.example.study.DTOs;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record BorrowRecordRequestDto(
        @NotNull
        @Min(1)
        Long userId,
        @NotNull
        @Min(1)
        Long bookId
)
{}
