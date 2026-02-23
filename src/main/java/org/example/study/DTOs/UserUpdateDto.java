package org.example.study.DTOs;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.study.enums.Gender;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserUpdateDto {

    @Min(0)
    @Max(value = 200, message = "Age should be less than 200 :)")
    private Integer age;

    @Size(min = 1, max = 100, message = "name is mandatory and its length should be in range of 1 - 100")
    private String fullName;

    private Gender gender;

    public static UserUpdateDto copyOf(UserUpdateDto dtoToCopy) {
        return new UserUpdateDto(dtoToCopy.getAge(),dtoToCopy.getFullName(),dtoToCopy.getGender());
    }
}
