package org.example.study.testData.DTOResolvers;

import org.example.study.Annotations.RandomInvalidUserDto;
import org.example.study.Annotations.RandomInvalidUserDtoList;
import org.example.study.StrategyEngine.FieldInvalidators.Factories.InvalidStrategyFactory;
import org.example.study.StrategyEngine.interfaces.InvalidDTOGenerationStrategy;
import org.example.study.testData.BaseParameterResolver;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

/**
 * Generates invalid UserDto instances with specific constraint violations.
 * Supports both single invalid objects and lists of invalid objects.
 * Properly resolves fields from parent classes (e.g., BaseUser fields in UserDto).
 */
public class RandomInvalidUserDtoResolver extends BaseParameterResolver {

    private InvalidStrategyFactory strategyFactory = new InvalidStrategyFactory();

    @Override
    public boolean supportsParameter(@NonNull ParameterContext parameterContext, @NonNull ExtensionContext extensionContext) throws ParameterResolutionException {

        boolean isEligibleForSingle = isAnnotatedWith(parameterContext, RandomInvalidUserDto.class);
        boolean isEligibleForList = isAnnotatedWith(parameterContext, RandomInvalidUserDtoList.class) &&
                isSuperOf(List.class, parameterContext) &&
                hasParametrizedType(parameterContext);
        return isEligibleForList || isEligibleForSingle;
    }

    @Override
    public @Nullable Object resolveParameter(@NonNull ParameterContext parameterContext, @NonNull ExtensionContext extensionContext) throws ParameterResolutionException {
        if (isAnnotatedWith(parameterContext, RandomInvalidUserDto.class)) {
            Class<?> rawType = parameterContext.getParameter().getType();
            RandomInvalidUserDto annotation = parameterContext.getParameter().getAnnotation(RandomInvalidUserDto.class);
            Class<? extends Annotation> constraintToBreak = annotation.constraintToBreak();
            String fieldName = annotation.fieldName();

            try {
                InvalidDTOGenerationStrategy pageStrategy = strategyFactory.getInvalidDTOGenerationStrategy(annotation.strategy());
                Field field = findField(rawType, fieldName);
                return pageStrategy.generateInvalidObj(rawType, field, constraintToBreak);
            } catch (NoSuchFieldException e) {
                throw new RuntimeException("Error generating invalid UserDto due to field mismatch: " + e.getMessage(), e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Invalid Class provided for object generation: " + e.getMessage(), e);
            }
        } else if (isAnnotatedWith(parameterContext, RandomInvalidUserDtoList.class)) {
            RandomInvalidUserDtoList annotation = parameterContext.getParameter().getAnnotation(RandomInvalidUserDtoList.class);

            int count = annotation.count();
            InvalidDTOGenerationStrategy strategy = strategyFactory.getInvalidDTOGenerationStrategy(annotation.strategy());

            try {
                return strategy.generateInvalidList(getGenericType(parameterContext), count);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Invalid Class provided for list generation: " + e.getMessage(), e);
            } catch (NoSuchFieldException e) {
                throw new RuntimeException("Error generating invalid a list due to field mismatch: " + e.getMessage(), e);
            }
        }

        throw new ParameterResolutionException(
                "Unsupported parameter type or missing annotation for parameter: " +
                        parameterContext.getParameter().getName()
        );
    }
}
