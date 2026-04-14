package org.example.study.testData.DTOResolvers;

import org.example.study.Annotations.RandomUserDto;
import org.example.study.Annotations.RandomUserDtoList;
import org.example.study.DTOs.UserDto;
import org.example.study.StrategyEngine.interfaces.ValidDTOGenerationStrategy;
import org.example.study.testData.BaseParameterResolver;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;

import java.util.List;

import static org.example.study.testData.TestData.getSingleValidUser;

public class RandomUserDtoResolver extends BaseParameterResolver {

    @Override
    public boolean supportsParameter(@NonNull ParameterContext parameterContext, @NonNull ExtensionContext extensionContext) throws ParameterResolutionException {

        boolean isEligibleForSingle = isAnnotatedWith(parameterContext, RandomUserDto.class) &&
                isSuperOf(UserDto.class, parameterContext);
        boolean isEligibleForList = isAnnotatedWith(parameterContext, RandomUserDtoList.class) &&
                isSuperOf(List.class, parameterContext) &&
                hasParametrizedType(parameterContext) &&
                isParametrizedTypeOf(UserDto.class, parameterContext);

        return isEligibleForList || isEligibleForSingle;
    }

    @Override
    public @Nullable Object resolveParameter(@NonNull ParameterContext parameterContext, @NonNull ExtensionContext extensionContext) throws ParameterResolutionException {
        if (isAnnotatedWith(parameterContext, RandomUserDto.class)) {
            return getSingleValidUser();
        } else if (isAnnotatedWith(parameterContext, RandomUserDtoList.class)) {
            RandomUserDtoList annotation = parameterContext.findAnnotation(RandomUserDtoList.class).
                    orElseThrow(() -> new ParameterResolutionException("Missing @RandomUserDtoList annotation"));
            int count = annotation.count();
            ValidDTOGenerationStrategy strategy = pageStrategyMap.get(annotation.strategy());
            validateStrategyType(annotation.strategy(), count);

            return strategy.generateValidList(UserDto.class, count);
        }

        throw new ParameterResolutionException(
                "Unsupported parameter type or missing annotation for parameter: " +
                        parameterContext.getParameter().getName()
        );
    }


}
