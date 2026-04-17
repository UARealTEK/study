package org.example.study.testData.PageResolvers;

import org.example.study.Annotations.RandomPageImplObj;
import org.example.study.StrategyEngine.FieldInvalidators.Factories.ValidStrategyFactory;
import org.example.study.StrategyEngine.interfaces.ValidDTOGenerationStrategy;
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

//TODO: look into my PageImpl resolvers. Make sure it works correctly in accordance to .size() and not to .totalElements()
// Implement realistic logic for generating a proper structure in case LAST page is requested (no overlapping in case size > amount of elements on the last page)
// refactor the logic in LIST portion of my resolvers
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
    public @Nullable Page<?> resolveParameter(ParameterContext parameterContext, @NonNull ExtensionContext extensionContext)
            throws ParameterResolutionException {
        ValidStrategyFactory factories = getValidFactory(extensionContext);
        RandomPageImplObj annotation = parameterContext.findAnnotation(RandomPageImplObj.class).
                orElseThrow(() -> new ParameterResolutionException("Missing @PageImplObj annotation"));
        int page = annotation.page();
        int size = annotation.size();
        int totalElements = annotation.totalElements();

        int start = page * size;
        int remaining = Math.max(0, totalElements - start);
        int currentPageSize = Math.min(size,remaining);
        ValidDTOGenerationStrategy strategy = factories.getValidDTOGenerationStrategy(annotation.strategy());

        validateStrategyType(annotation.strategy(), totalElements);

        Class<?> elementType = getGenericType(parameterContext);

        // Use the generic method to get the list
        List<?> list = strategy.generateValidList(elementType, currentPageSize);

        // Create and return the PageImpl
        return new PageImpl<>(list, PageRequest.of(page, size), totalElements);
    }

}
