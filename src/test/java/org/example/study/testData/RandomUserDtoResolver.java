package org.example.study.testData;

import org.example.study.Annotations.RandomUserDto;
import org.example.study.DTOs.UserDto;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import static org.example.study.testData.TestData.getSingleValidUser;

public class RandomUserDtoResolver implements ParameterResolver {

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, @NonNull ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.isAnnotated(RandomUserDto.class) && parameterContext.getParameter().getType().equals(UserDto.class);
    }

    @Override
    public @Nullable UserDto resolveParameter(@NonNull ParameterContext parameterContext, @NonNull ExtensionContext extensionContext) throws ParameterResolutionException {
        return getSingleValidUser();
    }
}
