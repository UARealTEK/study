package org.example.study.testData;

import net.datafaker.Faker;
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

//TODO: work on random data generation for tests
public class TestData {

    private static final UserMapper mapper = Mappers.getMapper(UserMapper.class);
    private static final Faker faker = new Faker();

    public static List<UserDto> getValidUsers() {
        return List.of(
                new UserDto(21, "Andrew", Gender.MALE),
                new UserDto(25, "Vova", Gender.MALE),
                new UserDto(31, "Dima", Gender.MALE));
    }

    public static Stream<Arguments> getValidUserStream() {
        return getValidUsers().stream().map(i -> Arguments.of(List.of(i)));
    }

    public static PageResponseDTO<UserDto> getValidUserDtoPage() {
        List<UserDto> list = getValidUsers();
        Page<UserDto> pageDto = new PageImpl<>(list, PageRequest.of(0,list.size()), list.size());
        return mapper.toPageResponse(pageDto);
    }

    public static Stream<Arguments> getValidUserDtoPageStream() {
        List<UserDto> list = getValidUsers();
        return Stream.of(
                Arguments.of(
                        mapper.toPageResponse(new PageImpl<>(list,
                                PageRequest.of(0,list.size()), list.size()))));
    }

    public static List<UserEntity> getValidEntities() {
        return List.of(
                new UserEntity(faker.number().numberBetween(0L,10L), faker.number().numberBetween(1,200), faker.funnyName().name(), Gender.random()),
                new UserEntity(faker.number().numberBetween(0L,10L), faker.number().numberBetween(1,200), faker.funnyName().name(), Gender.random()),
                new UserEntity(faker.number().numberBetween(0L,10L), faker.number().numberBetween(1,200), faker.funnyName().name(), Gender.random())
        );
    }

    public static Page<UserEntity> getValidUserEntityPage() {
        List<UserEntity> list = getValidEntities();
        return new PageImpl<>(list, PageRequest.of(0,list.size()), list.size());
    }

    public static UserDto getSingleValidUser() {
        return new UserDto(faker.number().numberBetween(1,200), faker.funnyName().name(), Gender.random());
    }

    public static UserEntity getSingleValidEntity() {
        return new UserEntity(faker.number().numberBetween(0L,10L), faker.number().numberBetween(1,200), faker.funnyName().name(), Gender.random());
    }

    public static UserDto getSingleUserWithEmptyName() {
        return new UserDto(faker.number().numberBetween(1,200), "",Gender.random());
    }

    public static UserEntity getSingleEntityWithEmptyName() {
        return new UserEntity(faker.number().numberBetween(0L,10L),faker.number().numberBetween(0,200), "",Gender.random());
    }
}
