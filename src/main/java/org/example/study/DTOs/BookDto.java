package org.example.study.DTOs;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class BookDto {

    @NotNull
    @Size(min = 1, max = 100, message = "name is mandatory and its length should be in range of 1 - 100")
    private String name;

    @NotNull
    @Size(min = 1, max = 100, message = "author is mandatory and its length should be in range of 1 - 100")
    private String author;
}
