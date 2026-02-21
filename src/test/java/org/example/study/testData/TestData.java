package org.example.study.testData;

import org.example.study.enums.Gender;
import org.example.study.DTOs.UserDto;
import org.example.study.Entities.UserEntity;

import java.util.List;

public class TestData {

    public static List<UserDto> getValidUsers() {
        return List.of(
                new UserDto(10, "Vova", Gender.MALE),
                new UserDto(15, "Nina", Gender.FEMALE),
                new UserDto(30, "Ivan", Gender.MALE));
    }

    public static List<UserEntity> getValidEntities() {
        return List.of(
                new UserEntity(1L,10, "Vova", Gender.MALE),
                new UserEntity(2L,15, "Nina", Gender.FEMALE),
                new UserEntity(3L,30, "Ivan", Gender.MALE));
    }

    public static UserDto getSingleValidUser() {
        return new UserDto(10, "Volodymyr", Gender.MALE);
    }

    public static UserEntity getSingleValidEntity() {
        return new UserEntity(4L, 15, "Andrew", Gender.MALE);
    }

    public static UserDto getSingleUserWithEmptyName() {
        return new UserDto(10, "",Gender.FEMALE);
    }

    public static UserEntity getSingleEntityWithEmptyName() {
        return new UserEntity(5L,33, "",Gender.FEMALE);
    }
}
