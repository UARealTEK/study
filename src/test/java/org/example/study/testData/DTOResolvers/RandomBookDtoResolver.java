package org.example.study.testData.DTOResolvers;

import org.example.study.Annotations.RandomBookDto;
import org.example.study.Annotations.RandomBookDtoList;
import org.example.study.DTOs.BookDto;
import org.example.study.StrategyEngine.FieldInvalidators.Factories.StrategyFactory;
import org.example.study.StrategyEngine.interfaces.ValidDTOGenerationStrategy;
import org.example.study.testData.BaseParameterResolver;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;

import java.util.List;

import static org.example.study.testData.TestData.getSingleValidBookDto;


//TODO: complete it
public class RandomBookDtoResolver extends BaseParameterResolver {

    private StrategyFactory factory;

    public RandomBookDtoResolver(StrategyFactory factory) {
        this.factory = factory;
    }

    @Override
    public boolean supportsParameter(@NonNull ParameterContext parameterContext, @NonNull ExtensionContext extensionContext) throws ParameterResolutionException {

        boolean isEligibleForSingle = isAnnotatedWith(parameterContext, RandomBookDto.class) &&
                isSuperOf(BookDto.class, parameterContext);
        boolean isEligibleForList = isAnnotatedWith(parameterContext, RandomBookDtoList.class) &&
                isSuperOf(List.class, parameterContext) &&
                hasParametrizedType(parameterContext) &&
                isParametrizedTypeOf(BookDto.class, parameterContext);

        return isEligibleForList || isEligibleForSingle;
    }

    @Override
    public @Nullable Object resolveParameter(@NonNull ParameterContext parameterContext, @NonNull ExtensionContext extensionContext) throws ParameterResolutionException {
        if (isAnnotatedWith(parameterContext, RandomBookDto.class)) {
            return getSingleValidBookDto();
        } else if (isAnnotatedWith(parameterContext, RandomBookDtoList.class)) {
            RandomBookDtoList annotation = parameterContext.findAnnotation(RandomBookDtoList.class).
                    orElseThrow(() -> new ParameterResolutionException("Missing @RandomBookDtoList annotation"));
            int count = annotation.count();
            ValidDTOGenerationStrategy strategy = factory.getValidDTOGenerationStrategy(annotation.strategy());
            validateStrategyType(annotation.strategy(), count);

            return strategy.generateValidList(BookDto.class, count);
        }

        throw new ParameterResolutionException(
                "Unsupported parameter type or missing annotation for parameter: " +
                        parameterContext.getParameter().getName()
        );
    }


}
