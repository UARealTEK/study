package org.example.study.DTOs;

import org.springframework.data.domain.Page;

import java.util.List;

public record PageResponseDTO<T>(List<T> content,
                                 int number,
                                 int size,
                                 long totalElements,
                                 int totalPages) {
}
