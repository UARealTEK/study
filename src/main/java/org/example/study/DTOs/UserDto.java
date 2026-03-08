package org.example.study.DTOs;

import jakarta.validation.constraints.*;
import lombok.*;
import org.example.study.enums.Gender;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDto {

    @NotNull(message = "Age should NOT be null")
    @Min(0)
    @Max(value = 200, message = "Age should be less than 200 :)")
    private Integer age;

    @NotBlank(message = "name should not be blank")
    @Size(min = 1, max = 100, message = "name is mandatory and its length should be in range of 1 - 100")
    private String fullName;

    @NotNull
    private Gender gender;

    //TODO: figure out why do I need it and if it is needed at all
    public static UserDto copyOf(UserDto dtoToCopy) {
        return new UserDto(dtoToCopy.getAge(),dtoToCopy.getFullName(),dtoToCopy.getGender());
    }
}
