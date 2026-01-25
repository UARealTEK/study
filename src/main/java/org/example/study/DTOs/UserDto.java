package org.example.study.DTOs;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDto {

    @Min(value = 1, message = "Age should be bigger than 1")
    @Max(value = 200, message = "Age should be less than 200 :)")
    private Integer age;

    @NotBlank(message = "name should not be blank")
    @Length(min = 1, max = 100, message = "name is mandatory and its length should be in range of 1 - 100")
    private String fullName;

    @NotNull
    private Gender gender;
}
