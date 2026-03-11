package org.example.study.testData;

import org.example.study.Annotations.PageImplObj;
import org.example.study.DTOs.BaseDao;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import static org.example.study.testData.TestData.getValidListForType;

public class RandomPageImplResolver implements ParameterResolver {
    @Override
    public boolean supportsParameter(ParameterContext parameterContext, @NonNull ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.isAnnotated(PageImplObj.class) && Page.class.isAssignableFrom(parameterContext.getParameter().getType());
    }

    @Override
    public @Nullable Object resolveParameter(ParameterContext parameterContext, @NonNull ExtensionContext extensionContext) throws ParameterResolutionException {
        PageImplObj annotation = parameterContext.findAnnotation(PageImplObj.class).
                orElseThrow(() -> new ParameterResolutionException("Missing @PageImplObj annotation"));
        int contentSize = annotation.contentSize();
        int page = annotation.page();
        int size = annotation.size();
        int totalElements = annotation.totalElements();

        if (contentSize > size) {
            throw new ParameterResolutionException("Content size (" + contentSize + ") cannot be greater than page size (" + size + ")");
        }

        @SuppressWarnings("unchecked")
        Class<BaseDao> elementType =
                parameterContext.getParameter().getType().getTypeParameters().length > 0
                        ? (Class<BaseDao>) ((ParameterizedType) parameterContext
                        .getParameter()
                        .getParameterizedType())
                        .getActualTypeArguments()[0]
                        : BaseDao.class;

        // Use the generic method to get the list
        List<BaseDao> list = getValidListForType(elementType, contentSize);

        // Create and return the PageImpl
        return new PageImpl<>(list, PageRequest.of(page, size), totalElements);
    }

}
