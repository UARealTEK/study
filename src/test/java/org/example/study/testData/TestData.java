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

@SuppressWarnings("unused")
public class TestData {

    private static final UserMapper mapper = Mappers.getMapper(UserMapper.class);
    private static final Faker faker = new Faker();

    // Generate a list of valid UserDto using Faker
    public static List<UserDto> getValidUsers(int count) {
        return Stream.generate(() -> new UserDto(faker.number().numberBetween(1, 200), faker.funnyName().name(), Gender.random()))
                .limit(count)
                .toList();
    }

    // Stream for parameterized tests
    public static Stream<Arguments> getValidUserStream(int count) {
        return Stream.of(Arguments.of(getValidUsers(count)));
    }

    // PageResponseDTO for UserDto
    public static PageResponseDTO<UserDto> getValidUserDtoPage(int count) {
        List<UserDto> list = getValidUsers(count);
        Page<UserDto> pageDto = new PageImpl<>(list, PageRequest.of(0, list.size()), list.size());
        return mapper.toPageResponse(pageDto);
    }

    // Stream for page DTOs
    public static Stream<Arguments> getValidUserDtoPageStream(int count) {
        return Stream.of(Arguments.of(getValidUserDtoPage(count)));
    }

    // Generate a list of valid UserEntities using Faker
    public static List<UserEntity> getValidEntities(int count) {
        return Stream.generate(() -> new UserEntity(faker.number().numberBetween(0L, 10L), faker.number().numberBetween(1, 200), faker.funnyName().name(), Gender.random()))
                .limit(count)
                .toList();
    }

    // Page for UserEntities
    public static Page<UserEntity> getValidUserEntityPage(int count) {
        List<UserEntity> list = getValidEntities(count);
        return new PageImpl<>(list, PageRequest.of(0, list.size()), list.size());
    }

    // Single UserDto
    public static UserDto getSingleValidUser() {
        return new UserDto(faker.number().numberBetween(1, 200), faker.funnyName().name(), Gender.random());
    }

    // Single UserEntity
    public static UserEntity getSingleValidEntity() {
        return new UserEntity(faker.number().numberBetween(0L, 10L), faker.number().numberBetween(1, 200), faker.funnyName().name(), Gender.random());
    }

    // Single UserDto with empty name
    public static UserDto getSingleUserWithEmptyName() {
        return new UserDto(faker.number().numberBetween(1, 200), "", Gender.random());
    }

    // Single UserEntity with empty name
    public static UserEntity getSingleEntityWithEmptyName() {
        return new UserEntity(faker.number().numberBetween(0L, 10L), faker.number().numberBetween(1, 200), "", Gender.random());
    }
}
