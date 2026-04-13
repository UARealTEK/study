package org.example.study.DTOs.Models;

import jakarta.validation.constraints.Size;

public record BookRequestModel(
        @Size(min = 1, max = 100, message = "name is mandatory and its length should be in range of 1 - 100")
        String name,
        @Size(min = 1, max = 100, message = "author is mandatory and its length should be in range of 1 - 100")
        String author) {
}
