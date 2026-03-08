package org.example.study.testData;

import org.example.study.Annotations.PageImplObj;
import org.example.study.Entities.UserEntity;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.stream.IntStream;

import static org.example.study.testData.TestData.getSingleValidEntity;

public class RandomPageImplResolver implements ParameterResolver {
    @Override
    public boolean supportsParameter(ParameterContext parameterContext, @NonNull ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.isAnnotated(PageImplObj.class) && Page.class.isAssignableFrom(parameterContext.getParameter().getType());
    }

    @Override
    public @Nullable Page<UserEntity> resolveParameter(ParameterContext parameterContext, @NonNull ExtensionContext extensionContext) throws ParameterResolutionException {
        PageImplObj annotation = parameterContext.findAnnotation(PageImplObj.class).
                orElseThrow(() -> new ParameterResolutionException("Missing @PageableObj annotation"));
        int page = annotation.page();
        int size = annotation.size();
        int totalElements = annotation.totalElements();
        return new PageImpl<>(generateContent(size), PageRequest.of(page, size), totalElements);
    }

    private List<UserEntity> generateContent(int size) {
        return IntStream.range(0,size).mapToObj(
                i -> getSingleValidEntity()
        ).toList();
    }
}
