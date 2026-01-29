package org.example.study.DTOs;

import jakarta.validation.constraints.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDto {

    @NotNull(message = "Age should NOT be null")
    @Max(value = 200, message = "Age should be less than 200 :)")
    private Integer age;

    @NotBlank(message = "name should not be blank")
    @Size(max = 100, message = "name is mandatory and its length should be in range of 1 - 100")
    private String fullName;

    @NotNull
    private Gender gender;
}
