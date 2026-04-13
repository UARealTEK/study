package org.example.study.DTOs.Models;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import org.example.study.enums.Gender;

public record UserRequestModel(
        @Min(value = 1, message = "Integer should be 1 or bigger")
        @Max(value = 100, message = "Integer should be 100 or less")
        Integer age,
        @Size(min = 1, max = 100, message = "name is mandatory and its length should be in range of 1 - 100")
        String fullName,
        Gender gender) {
}
