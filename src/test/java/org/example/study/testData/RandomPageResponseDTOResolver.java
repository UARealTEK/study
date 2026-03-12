package org.example.study.testData;

import org.example.study.Annotations.RandomPageResponseDto;
import org.example.study.DTOs.BaseDao;
import org.example.study.DTOs.PageResponseDTO;
import org.example.study.DTOs.UserDto;
import org.example.study.StrategyEngine.interfaces.PageGenerationStrategy;
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

        return isParametrizedTypeOf(BaseDao.class, parameterContext);
    }

    @Override
    public @Nullable PageResponseDTO<?> resolveParameter(@NonNull ParameterContext parameterContext, @NonNull ExtensionContext extensionContext) throws ParameterResolutionException {
        RandomPageResponseDto annotation = parameterContext.findAnnotation(RandomPageResponseDto.class)
                .orElseThrow(() -> new ParameterResolutionException("Missing @RandomPageResponseDto annotation"));
        int page = annotation.number();
        int size = annotation.size();
        int totalElements = annotation.totalElements();
        int totalPages = (int) Math.ceil((double) totalElements / size);
        PageGenerationStrategy strategy = strategyMap.get(annotation.strategy());

        List<?> data = strategy.generate(UserDto.class, totalElements);

        return new PageResponseDTO<>(data, page, size, totalElements, totalPages);
    }
}
