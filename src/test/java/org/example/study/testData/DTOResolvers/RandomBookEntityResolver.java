package org.example.study.testData.DTOResolvers;

import org.example.study.Annotations.RandomBookEntity;
import org.example.study.Annotations.RandomBookEntityList;
import org.example.study.DTOs.Entities.BookEntity;
import org.example.study.StrategyEngine.interfaces.PageGenerationStrategy;
import org.example.study.testData.BaseParameterResolver;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;

import java.util.List;

import static org.example.study.testData.TestData.getSingleValidBook;

public class RandomBookEntityResolver extends BaseParameterResolver {

    @Override
    public boolean supportsParameter(@NonNull ParameterContext parameterContext, @NonNull ExtensionContext extensionContext) throws ParameterResolutionException {

        boolean isEligibleForSingle = isAnnotatedWith(parameterContext, RandomBookEntity.class) &&
                isSuperOf(BookEntity.class, parameterContext);
        boolean isEligibleForList = isAnnotatedWith(parameterContext, RandomBookEntityList.class) &&
                isSuperOf(List.class, parameterContext) &&
                hasParametrizedType(parameterContext) &&
                isParametrizedTypeOf(BookEntity.class, parameterContext);

        return isEligibleForList || isEligibleForSingle;
    }

    @Override
    public @Nullable Object resolveParameter(@NonNull ParameterContext parameterContext, @NonNull ExtensionContext extensionContext) throws ParameterResolutionException {
        if (isAnnotatedWith(parameterContext, RandomBookEntity.class)) {
            return getSingleValidBook();
        } else if (isAnnotatedWith(parameterContext, RandomBookEntityList.class)) {
            RandomBookEntityList annotation = parameterContext.findAnnotation(RandomBookEntityList.class).
                    orElseThrow(() -> new ParameterResolutionException("Missing @RandomBookEntityList annotation"));
            int count = annotation.count();
            PageGenerationStrategy strategy = pageStrategyMap.get(annotation.strategy());
            validateStrategyType(annotation.strategy(), count);

            return strategy.generate(BookEntity.class, count);
        }

        throw new ParameterResolutionException(
                "Unsupported parameter type or missing annotation for parameter: " +
                        parameterContext.getParameter().getName()
        );
    }


}
