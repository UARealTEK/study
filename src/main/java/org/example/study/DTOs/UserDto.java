package org.example.study.DTOs;

import jakarta.validation.constraints.*;
import lombok.*;
import org.example.study.enums.Gender;

@NoArgsConstructor
@Setter
@ToString
public class UserDto extends BaseUser {

    public UserDto(Integer age, String fullName, Gender gender) {
        setAge(age);
        setFullName(fullName);
        setGender(gender);
    }

    @Override
    @NotNull(message = "Age should NOT be null")
    @Min(value = 0, message = "Age should be greater than or equal to 0")
    @Max(value = 200, message = "Age should be less than 200 :)")
    public Integer getAge() {
        return super.getAge();
    }

    @Override
    @NotBlank(message = "name should not be blank")
    @Size(min = 1, max = 100, message = "name is mandatory and its length should be in range of 1 - 100")
    public String getFullName() {
        return super.getFullName();
    }

    //TODO: how do I validate invalid input for GENDER ?
    @NotNull
    public Gender getGender() {
        return super.getGender();
    }

    public static UserDto copy(UserDto dtoToCopy) {
        return new UserDto(dtoToCopy.getAge(),dtoToCopy.getFullName(),dtoToCopy.getGender());
    }
}
