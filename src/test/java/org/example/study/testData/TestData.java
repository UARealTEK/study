package org.example.study.testData;

import org.example.study.DTOs.Gender;
import org.example.study.DTOs.UserDto;

import java.util.List;

public class TestData {

    public static List<UserDto> getValidUsers() {
        return List.of(
                new UserDto(10, "Vova", Gender.MALE),
                new UserDto(15, "Nina", Gender.FEMALE),
                new UserDto(30, "Ivan", Gender.MALE));
    }

    public static UserDto getSingleValidUser() {
        return new UserDto(10, "Volodymyr", Gender.MALE);
    }
}
