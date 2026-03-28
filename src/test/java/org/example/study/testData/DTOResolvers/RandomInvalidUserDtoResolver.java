package org.example.study.testData.DTOResolvers;

import org.example.study.Annotations.RandomInvalidUserDto;
import org.example.study.Annotations.RandomInvalidUserDtoList;
import org.example.study.Annotations.RandomUserDtoList;
import org.example.study.DTOs.UserDto;
import org.example.study.StrategyEngine.interfaces.InvalidDTOGenerationStrategy;
import org.example.study.StrategyEngine.interfaces.PageGenerationStrategy;
import org.example.study.enums.UserDTOInvalidFlag;
import org.example.study.testData.BaseParameterResolver;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;

import java.util.List;

//TODO: complete this one using refactored InvalidDTOGenerationStrategy
// FYI: its time to refactor it based on the Annotation parameters and NOT based on the flag (?)
// this also supports ONLY UserDto (?) Since Ive expanded my invalidator - refactor this as well
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
            UserDTOInvalidFlag flag = parameterContext.findAnnotation(RandomInvalidUserDto.class)
                    .orElseThrow(() -> new ParameterResolutionException("Missing @RandomInvalidUserDto annotation"))
                    .invalidFlag();

            InvalidDTOGenerationStrategy strategy = invalidDtoStrategyMap.get(flag);
            try {
                return strategy.generate(UserDto.class);
            } catch (NoSuchFieldException e) {
                throw new RuntimeException("Error generating invalid UserDto due to field mismatch: " + e.getMessage(), e);
            }
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
