package org.example.study.testData;

import net.datafaker.Faker;
import org.example.study.DTOs.BookDto;
import org.example.study.DTOs.BorrowRecordResponseDto;
import org.example.study.DTOs.Entities.BookEntity;
import org.example.study.DTOs.Entities.BorrowRecordEntity;
import org.example.study.DTOs.PageResponseDTO;
import org.example.study.DTOs.UserDto;
import org.example.study.DTOs.Entities.UserEntity;
import org.example.study.util.Converters.UserMapper;
import org.junit.jupiter.params.provider.Arguments;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.example.study.enums.Gender.random;

@SuppressWarnings("unused")
public class TestData {

    private static final UserMapper mapper = Mappers.getMapper(UserMapper.class);
    private static final Faker faker = new Faker();

    private static final Map<Class<?>, Function<Integer, List<?>>> generators = Map.of(
            UserDto.class, TestData::getValidUsers,
            BookDto.class, TestData::getValidBooks,
            UserEntity.class, TestData::getValidEntities,
            BookEntity.class, TestData::getValidBookEntities,
            BorrowRecordEntity.class, TestData::getValidRecordEntities,
            BorrowRecordResponseDto.class, TestData::getValidBorrowRecordDTOs
    );

    private static final Map<Class<?>, Supplier<?>> singleGenerators = Map.of(
            UserDto.class, TestData::getSingleValidUser,
            BookDto.class, TestData::getSingleValidBookDto,
            UserEntity.class, TestData::getSingleValidEntity,
            BookEntity.class, TestData::getSingleValidBook,
            BorrowRecordEntity.class, TestData::getSingleValidRecordEntity,
            BorrowRecordResponseDto.class, TestData::getSingleValidRecordDto
    );

    // Generate a list of valid UserDto using Faker
    private static List<UserDto> getValidUsers(int count) {
        return Stream.generate(() -> new UserDto(faker.number().numberBetween(1, 200), faker.funnyName().name(), random()))
                .limit(count)
                .toList();
    }

    // Generate a list of valid UserDto using Faker
    private static List<BookDto> getValidBooks(int count) {
        return Stream.generate(() -> BookDto.builder()
                        .name(faker.book().title())
                        .author(faker.book().author())
                        .build()
                )
                .limit(count)
                .toList();
    }

    // Generate a list of valid UserEntities using Faker
    private static List<UserEntity> getValidEntities(int count) {
        return Stream.generate(() -> new UserEntity(
                                faker.number().numberBetween(0L, 10L),
                                faker.number().numberBetween(1, 200),
                                faker.funnyName().name(), random())
                )
                .limit(count)
                .toList();
    }

    //Generate a list of valid Books using Faker
    private static List<BookEntity> getValidBookEntities(int count) {
        return Stream.generate(() -> BookEntity.builder()
                        .id(faker.number().numberBetween(0L, 10L))
                        .name(faker.book().title())
                        .author(faker.book().author())
                        .build()
                )
                .limit(count)
                .toList();
    }

    private static List<BorrowRecordEntity> getValidRecordEntities(int count) {
        return Stream.generate(TestData::getSingleValidRecordEntity)
                .limit(count)
                .toList();
    }

    private static List<BorrowRecordResponseDto> getValidBorrowRecordDTOs(int count) {
        return Stream.generate(TestData::getSingleValidRecordDto)
                .limit(count)
                .toList();
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> getValidListForType(Class<T> clazz, int count) {
        Function<Integer, List<?>> generator = generators.get(clazz);
        if (generator == null) {
            throw new IllegalArgumentException("Unsupported class type: " + clazz.getName());
        }
        return (List<T>) generator.apply(count);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getSingleValidForType(Class<T> clazz) {
        Supplier<?> generator = singleGenerators.get(clazz);
        if (generator == null) {
            throw new IllegalArgumentException("Unsupported class type: " + clazz.getName());
        }
        return (T) generator.get();
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

    // Page for UserEntities
    public static Page<UserEntity> getValidUserEntityPage(int count) {
        List<UserEntity> list = getValidEntities(count);
        return new PageImpl<>(list, PageRequest.of(0, list.size()), list.size());
    }

    // Single UserDto
    public static UserDto getSingleValidUser() {
        return new UserDto(faker.number().numberBetween(1, 200), faker.funnyName().name(), random());
    }

    // Single UserEntity
    public static UserEntity getSingleValidEntity() {
        return new UserEntity(faker.number().numberBetween(0L, 10L), faker.number().numberBetween(1, 200), faker.funnyName().name(), random());
    }

    // Single UserDto with empty name
    public static UserDto getSingleUserWithEmptyName() {
        return new UserDto(faker.number().numberBetween(1, 200), "", random());
    }

    public static BookEntity getSingleValidBook() {
        return BookEntity.builder()
                .id(faker.number().numberBetween(0L, 10L))
                .name(faker.book().title())
                .author(faker.book().author())
                .build();
    }

    public static BookDto getSingleValidBookDto() {
        return BookDto.builder()
                .name(faker.book().title())
                .author(faker.book().author())
                .build();
    }

    //generates a record with FILLED borrowedAt / returnedAt variables
    public static BorrowRecordEntity getSingleValidRecordEntity() {
        var borrowedAtDateTime = faker.timeAndDate().birthday().atStartOfDay();
        var returnedAtDateTime = borrowedAtDateTime.plusDays(ThreadLocalRandom.current().nextInt(1, 10));
        return BorrowRecordEntity.builder()
                .id(faker.number().numberBetween(0L, 10L))
                .user(getSingleValidEntity())
                .book(getSingleValidBook())
                .borrowedAt(borrowedAtDateTime)
                .returnedAt(returnedAtDateTime)
                .build();
    }

    public static BorrowRecordResponseDto getSingleValidRecordDto() {
        var borrowedAtDateTime = faker.timeAndDate().birthday().atStartOfDay();
        var returnedAtDateTime = borrowedAtDateTime.plusDays(ThreadLocalRandom.current().nextInt(1, 10));
        return BorrowRecordResponseDto.builder()
                .book(getSingleValidBookDto())
                .userName(faker.name().fullName())
                .borrowedAt(borrowedAtDateTime)
                .returnedAt(returnedAtDateTime)
                .build();
    }

    // Single UserEntity with empty name
    public static UserEntity getSingleEntityWithEmptyName() {
        return new UserEntity(faker.number().numberBetween(0L, 10L), faker.number().numberBetween(1, 200), "", random());
    }

    // Generate a list of UserDto with fixed values
    public static List<UserDto> getValidUsersWithFixedValues(int count) {
        UserDto dto = new UserDto(faker.number().numberBetween(1, 200), faker.funnyName().name(), random());
        return Stream.generate(() -> new UserDto(dto.getAge(),dto.getFullName(), dto.getGender()))
                .limit(count)
                .toList();
    }
}
