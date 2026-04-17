package org.example.study.testData.PageResolvers;

import org.example.study.Annotations.RandomPageResponseDto;
import org.example.study.DTOs.PageResponseDTO;
import org.example.study.StrategyEngine.FieldInvalidators.Factories.ValidStrategyFactory;
import org.example.study.StrategyEngine.interfaces.ValidDTOGenerationStrategy;
import org.example.study.testData.BaseParameterResolver;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;

import java.util.List;

@SuppressWarnings("unused")
public class RandomPageResponseDTOResolver extends BaseParameterResolver {

    @Override
    public boolean supportsParameter(@NonNull ParameterContext parameterContext, @NonNull ExtensionContext extensionContext) throws ParameterResolutionException {
        // Check if annotated with @PageImplObj
        if (!isAnnotatedWith(parameterContext, RandomPageResponseDto.class)) return false;

        // Check if parameter type is Page
        if (!isSuperOf(PageResponseDTO.class, parameterContext)) return false;

        // Check if it's a parameterized type (e.g., Page<SomeDao>)
        if (!hasParametrizedType(parameterContext)) return false;

        //TODO: why Object?
        return isParametrizedTypeOf(Object.class, parameterContext);
    }

    @Override
    public @Nullable PageResponseDTO<?> resolveParameter(@NonNull ParameterContext parameterContext, @NonNull ExtensionContext extensionContext) throws ParameterResolutionException {
        ValidStrategyFactory factory = getValidFactory(extensionContext);
        RandomPageResponseDto annotation = parameterContext.findAnnotation(RandomPageResponseDto.class)
                .orElseThrow(() -> new ParameterResolutionException("Missing @RandomPageResponseDto annotation"));
        int page = annotation.number();
        int size = annotation.size();
        int totalElements = annotation.totalElements();

        int start = page * size;
        int remaining = Math.max(0, totalElements - start);
        int currentPageSize = Math.min(size,remaining);
        int totalPages = (int) Math.ceil((double) totalElements / size); // manually calculate totalPages to ensure consistency with the provided totalElements and size
        ValidDTOGenerationStrategy strategy = factory.getValidDTOGenerationStrategy(annotation.strategy());

        validateStrategyType(annotation.strategy(), totalElements);
        Class<?> elementType = getGenericType(parameterContext);

        List<?> data = strategy.generateValidList(elementType, currentPageSize);

        return new PageResponseDTO<>(data, page, size, totalElements, totalPages);
    }
}
