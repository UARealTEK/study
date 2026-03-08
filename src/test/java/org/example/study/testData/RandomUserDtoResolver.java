package org.example.study.testData;

import net.datafaker.Faker;
import org.example.study.Annotations.RandomUserDtoBody;
import org.example.study.DTOs.UserDto;
import org.example.study.enums.Gender;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

public class RandomUserDtoResolver implements ParameterResolver {

    private static final Faker faker = new Faker();

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, @NonNull ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.isAnnotated(RandomUserDtoBody.class);
    }

    @Override
    public @Nullable Object resolveParameter(ParameterContext parameterContext, @NonNull ExtensionContext extensionContext) throws ParameterResolutionException {

        if (parameterContext.getParameter().getType().equals(UserDto.class)) {
            return getRandomUser();
        }
        return null;
    }

    private UserDto getRandomUser() {
        return new UserDto(
                faker.number().numberBetween(1,200),
                faker.funnyName().name(),
                Gender.random()
        );
    }
}
