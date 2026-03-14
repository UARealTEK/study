package org.example.study.testData;

import org.example.study.Annotations.RandomInvalidUserDto;
import org.example.study.Annotations.RandomInvalidUserDtoList;
import org.example.study.Annotations.RandomUserDtoList;
import org.example.study.DTOs.UserDto;
import org.example.study.StrategyEngine.interfaces.PageGenerationStrategy;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;

import java.util.List;

import static org.example.study.testData.TestData.getSingleValidUser;

public class RandomInvalidUserDtoResolver extends BaseParameterResolver {

    @Override
    public boolean supportsParameter(@NonNull ParameterContext parameterContext, @NonNull ExtensionContext extensionContext) throws ParameterResolutionException {

        boolean isEligibleForSingle = isAnnotatedWith(parameterContext, RandomInvalidUserDto.class) &&
                isSuperOf(UserDto.class, parameterContext);
        boolean isEligibleForList = isAnnotatedWith(parameterContext, RandomInvalidUserDtoList.class) &&
                isSuperOf(List.class, parameterContext) &&
                hasParametrizedType(parameterContext) &&
                isParametrizedTypeOf(UserDto.class, parameterContext);

        return isEligibleForList || isEligibleForSingle;
    }

    @Override
    public @Nullable Object resolveParameter(@NonNull ParameterContext parameterContext, @NonNull ExtensionContext extensionContext) throws ParameterResolutionException {
        if (isAnnotatedWith(parameterContext, RandomInvalidUserDto.class)) {
            return getSingleValidUser();
        } else if (isAnnotatedWith(parameterContext, RandomUserDtoList.class)) {
            RandomInvalidUserDtoList annotation = parameterContext.findAnnotation(RandomInvalidUserDtoList.class).
                    orElseThrow(() -> new ParameterResolutionException("Missing @RandomInvalidUserDtoList annotation"));
            int count = annotation.count();
            PageGenerationStrategy strategy = pageStrategyMap.get(annotation.strategy());
            validateStrategyType(annotation.strategy(), count);

            return strategy.generate(UserDto.class, count);
        }

        throw new ParameterResolutionException(
                "Unsupported parameter type or missing annotation for parameter: " +
                        parameterContext.getParameter().getName()
        );
    }


}
