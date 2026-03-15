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
@SuppressWarnings("unused")
public class UserPatchDto extends BaseUser {

    public UserPatchDto(Integer age, String fullName, Gender gender) {
        setAge(age);
        setFullName(fullName);
        setGender(gender);
    }

    @Override
    @Min(0)
    @Max(value = 200, message = "Age should be less than 200 :)")
    public Integer getAge() {
        return super.getAge();
    }

    @Override
    @Size(min = 1, max = 100, message = "name is mandatory and its length should be in range of 1 - 100")
    public String getFullName() {
        return super.getFullName();
    }

    @Override
    public Gender getGender() {
        return super.getGender();
    }
}
