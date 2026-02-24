package org.example.study.testData;

import org.example.study.DTOs.PageResponseDTO;
import org.example.study.enums.Gender;
import org.example.study.DTOs.UserDto;
import org.example.study.Entities.UserEntity;
import org.example.study.util.Converters.UserMapper;
import org.junit.jupiter.params.provider.Arguments;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.stream.Stream;

public class TestData {

    protected static final UserMapper mapper = Mappers.getMapper(UserMapper.class);

    public static List<UserDto> getValidUsers() {
        return List.of(
                new UserDto(10, "Vova", Gender.MALE),
                new UserDto(15, "Nina", Gender.FEMALE),
                new UserDto(30, "Ivan", Gender.MALE));
    }

    public static PageResponseDTO<UserDto> getValidUserDtoPage() {
        List<UserDto> list = getValidUsers();
        Page<UserDto> pageDto = new PageImpl<>(list, PageRequest.of(0,list.size()), list.size());
        return mapper.toPageResponse(pageDto);
    }

    public static Stream<Arguments> getValidUserDtoPageStream() {
        List<UserDto> list = getValidUsers();
        return Stream.of(Arguments.of(mapper.toPageResponse(new PageImpl<>(list, PageRequest.of(0,list.size()), list.size()))));
    }

    public static List<UserEntity> getValidEntities() {
        return List.of(
                new UserEntity(1L,10, "Vova", Gender.MALE),
                new UserEntity(2L,15, "Nina", Gender.FEMALE),
                new UserEntity(3L,30, "Ivan", Gender.MALE));
    }

    public static Page<UserEntity> getValidUserEntityPage() {
        List<UserEntity> list = getValidEntities();
        return new PageImpl<>(list, PageRequest.of(0,list.size()), list.size());
    }

    public static UserDto getSingleValidUser() {
        return new UserDto(10, "Volodymyr", Gender.MALE);
    }

    public static Stream<Arguments> getSingleValidUserArg() {
        return Stream.of(Arguments.of(new UserDto(10, "Volodymyr", Gender.MALE)));
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
