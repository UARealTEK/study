package org.example.study.testData.DTOResolvers;

import org.example.study.Annotations.RandomInvalidUserDto;
import org.example.study.Annotations.RandomInvalidUserDtoList;
import org.example.study.Annotations.RandomUserDtoList;
import org.example.study.DTOs.UserDto;
import org.example.study.StrategyEngine.DTOStrategies.GenericDtoInvalidStrategy;
import org.example.study.StrategyEngine.interfaces.PageGenerationStrategy;
import org.example.study.testData.BaseParameterResolver;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

//TODO: complete this one using refactored InvalidDTOGenerationStrategy
// FYI: its time to refactor it based on the Annotation parameters and NOT based on the flag (?)
// this also supports ONLY UserDto (?) Since Ive expanded my invalidator - refactor this as well
public class RandomInvalidUserDtoResolver extends BaseParameterResolver {

    @Override
    public boolean supportsParameter(@NonNull ParameterContext parameterContext, @NonNull ExtensionContext extensionContext) throws ParameterResolutionException {

        boolean isEligibleForSingle = isAnnotatedWith(parameterContext, RandomInvalidUserDto.class);
        boolean isEligibleForList = isAnnotatedWith(parameterContext, RandomInvalidUserDtoList.class) &&
                isSuperOf(List.class, parameterContext) &&
                hasParametrizedType(parameterContext);
        return isEligibleForList || isEligibleForSingle;
    }

    @SuppressWarnings("unchecked")
    @Override
    public @Nullable Object resolveParameter(@NonNull ParameterContext parameterContext, @NonNull ExtensionContext extensionContext) throws ParameterResolutionException {
        if (isAnnotatedWith(parameterContext, RandomInvalidUserDto.class)) {
            Class<?> rawType = parameterContext.getParameter().getType();
            RandomInvalidUserDto annotation = parameterContext.getParameter().getAnnotation(RandomInvalidUserDto.class);
            Class<? extends Annotation> annotationToBreak = annotation.constraintToBreak();
            String fieldName = annotation.fieldName();

            try {
                Field field = rawType.getDeclaredField(fieldName);
                Class<Object> clazz = (Class<Object>) field.getType();
                return new GenericDtoInvalidStrategy().generate(clazz,field,annotationToBreak);
            } catch (NoSuchFieldException e) {
                throw new RuntimeException("Error generating invalid UserDto due to field mismatch: " + e.getMessage(), e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Invalid Class provided");
            }
            //TODO: finish this one. Probably need a separate Generator to support LIST (?)
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
