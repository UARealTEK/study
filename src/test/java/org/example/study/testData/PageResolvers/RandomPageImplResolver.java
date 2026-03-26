package org.example.study.testData.PageResolvers;

import org.example.study.Annotations.RandomPageImplObj;
import org.example.study.StrategyEngine.interfaces.PageGenerationStrategy;
import org.example.study.testData.BaseParameterResolver;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

//TODO: make it generic ? so it can be used not only for BaseUser but for any type ?
public class RandomPageImplResolver extends BaseParameterResolver {

    @Override
    public boolean supportsParameter(@NonNull ParameterContext parameterContext, @NonNull ExtensionContext extensionContext) throws ParameterResolutionException {
        // Check if annotated with @PageImplObj
        if (!isAnnotatedWith(parameterContext, RandomPageImplObj.class)) return false;

        // Check if parameter type is Page
        if (!isSuperOf(Page.class, parameterContext)) return false;

        // Check if it's a parameterized type (e.g., Page<SomeDao>)
        if (!hasParametrizedType(parameterContext)) return false;

        return isParametrizedTypeOf(Object.class, parameterContext);
    }

    @Override
    public @Nullable Page<?> resolveParameter(ParameterContext parameterContext, @NonNull ExtensionContext extensionContext) throws ParameterResolutionException {
        RandomPageImplObj annotation = parameterContext.findAnnotation(RandomPageImplObj.class).
                orElseThrow(() -> new ParameterResolutionException("Missing @PageImplObj annotation"));
        int page = annotation.page();
        int size = annotation.size();
        int totalElements = annotation.totalElements();
        PageGenerationStrategy strategy = pageStrategyMap.get(annotation.strategy());

        validateStrategyType(annotation.strategy(), totalElements);

        Class<?> elementType = getGenericType(parameterContext);

        // Use the generic method to get the list
        List<?> list = strategy.generate(elementType, totalElements);

        // Create and return the PageImpl
        return new PageImpl<>(list, PageRequest.of(page, size), totalElements);
    }

}
