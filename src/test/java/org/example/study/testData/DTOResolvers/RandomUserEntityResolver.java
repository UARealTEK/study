package org.example.study.testData.DTOResolvers;

import org.example.study.Annotations.RandomUserEntity;
import org.example.study.Annotations.RandomUserEntityList;
import org.example.study.DTOs.Entities.UserEntity;
import org.example.study.StrategyEngine.interfaces.PageGenerationStrategy;
import org.example.study.testData.BaseParameterResolver;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;

import java.util.List;

import static org.example.study.testData.TestData.getSingleValidEntity;

//TODO: look into it
public class RandomUserEntityResolver extends BaseParameterResolver {

    @Override
    public boolean supportsParameter(@NonNull ParameterContext parameterContext, @NonNull ExtensionContext extensionContext) throws ParameterResolutionException {

        boolean isEligibleForSingle = isAnnotatedWith(parameterContext, RandomUserEntity.class) &&
                isSuperOf(UserEntity.class, parameterContext);
        boolean isEligibleForList = isAnnotatedWith(parameterContext, RandomUserEntityList.class) &&
                isSuperOf(List.class, parameterContext) &&
                hasParametrizedType(parameterContext) &&
                isParametrizedTypeOf(UserEntity.class, parameterContext);

        return isEligibleForList || isEligibleForSingle;
    }

    @Override
    public @Nullable Object resolveParameter(@NonNull ParameterContext parameterContext, @NonNull ExtensionContext extensionContext) throws ParameterResolutionException {
        if (isAnnotatedWith(parameterContext, RandomUserEntity.class)) {
            return getSingleValidEntity();
        } else if (isAnnotatedWith(parameterContext, RandomUserEntityList.class)) {
            RandomUserEntityList annotation = parameterContext.findAnnotation(RandomUserEntityList.class).
                    orElseThrow(() -> new ParameterResolutionException("Missing @RandomUserEntityList annotation"));
            int count = annotation.count();
            PageGenerationStrategy strategy = pageStrategyMap.get(annotation.strategy());
            validateStrategyType(annotation.strategy(), count);

            return strategy.generate(UserEntity.class, count);
        }

        throw new ParameterResolutionException(
                "Unsupported parameter type or missing annotation for parameter: " +
                        parameterContext.getParameter().getName()
        );
    }
}
