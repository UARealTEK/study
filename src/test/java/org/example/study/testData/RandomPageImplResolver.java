package org.example.study.testData;

import org.example.study.Annotations.RandomPageImplObj;
import org.example.study.DTOs.BaseDao;
import org.example.study.StrategyEngine.Strategies.EmptyStrategy;
import org.example.study.StrategyEngine.Strategies.RandomStrategy;
import org.example.study.StrategyEngine.Strategies.SameObjStrategy;
import org.example.study.StrategyEngine.interfaces.PageGenerationStrategy;
import org.example.study.enums.PageStrategyType;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Map;

public class RandomPageImplResolver extends BaseParameterResolver {

    private static final Map<PageStrategyType, PageGenerationStrategy> strategyMap = Map.of(
            PageStrategyType.RANDOM, new RandomStrategy(),
            PageStrategyType.SAME, new SameObjStrategy(),
            PageStrategyType.EMPTY, new EmptyStrategy()
    );

    @Override
    public boolean supportsParameter(@NonNull ParameterContext parameterContext, @NonNull ExtensionContext extensionContext) throws ParameterResolutionException {
        // Check if annotated with @PageImplObj
        if (!isAnnotatedWith(parameterContext, RandomPageImplObj.class)) return false;

        // Check if parameter type is Page
        if (!isSuperOf(Page.class, parameterContext)) return false;

        // Check if it's a parameterized type (e.g., Page<SomeDao>)
        if (!hasParametrizedType(parameterContext)) return false;

        return isParametrizedTypeOf(BaseDao.class, parameterContext);
    }

    @Override
    public @Nullable Page<?> resolveParameter(ParameterContext parameterContext, @NonNull ExtensionContext extensionContext) throws ParameterResolutionException {
        RandomPageImplObj annotation = parameterContext.findAnnotation(RandomPageImplObj.class).
                orElseThrow(() -> new ParameterResolutionException("Missing @PageImplObj annotation"));
        int page = annotation.page();
        int size = annotation.size();
        int totalElements = annotation.totalElements();
        PageGenerationStrategy strategy = strategyMap.get(annotation.strategy());

        validateAnnotation(annotation.strategy(), totalElements);

        Class<?> elementType = getGenericType(parameterContext);

        // Use the generic method to get the list
        List<?> list = strategy.generate(elementType, totalElements);

        // Create and return the PageImpl
        return new PageImpl<>(list, PageRequest.of(page, size), totalElements);
    }

    private void validateAnnotation(PageStrategyType type, int totalElements) {
        if (type == PageStrategyType.EMPTY && totalElements != 0) {
            throw  new ParameterResolutionException("totalElements must be 0 for EMPTY strategy");
        }
        if (type == PageStrategyType.RANDOM && totalElements <= 0) {
            throw  new ParameterResolutionException("totalElements must be bigger than 0 for RANDOM strategy");
        }
        if (type == PageStrategyType.SAME && totalElements <= 0) {
            throw  new ParameterResolutionException("totalElements must be bigger than 0 for SAME strategy");
        }
    }

}
