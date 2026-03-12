package org.example.study.testData;

import org.example.study.Annotations.RandomPageResponseDto;
import org.example.study.DTOs.BaseDao;
import org.example.study.DTOs.PageResponseDTO;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;

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

    //TODO: complete it
    @Override
    public @Nullable Object resolveParameter(@NonNull ParameterContext parameterContext, @NonNull ExtensionContext extensionContext) throws ParameterResolutionException {
        return null;
    }
}
