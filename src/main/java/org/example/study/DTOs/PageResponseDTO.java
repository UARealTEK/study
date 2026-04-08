package org.example.study.DTOs;

import lombok.Getter;

import java.util.List;

public record PageResponseDTO<T>(List<T> content,
                                 int number,
                                 int size,
                                 long totalElements,
                                 int totalPages) {
}
