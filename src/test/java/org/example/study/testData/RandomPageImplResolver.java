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
import java.lang.reflect.Type;
import java.util.List;

import static org.example.study.testData.TestData.getValidListForType;

public class RandomPageImplResolver implements ParameterResolver {
    @Override
    public boolean supportsParameter(ParameterContext parameterContext, @NonNull ExtensionContext extensionContext) throws ParameterResolutionException {
        // Check if annotated with @PageImplObj
        if (!parameterContext.isAnnotated(PageImplObj.class)) {
            return false;
        }

        // Check if parameter type is Page
        if (!Page.class.isAssignableFrom(parameterContext.getParameter().getType())) {
            return false;
        }

        // Check if it's a parameterized type (e.g., Page<SomeDao>)
        Type paramType = parameterContext.getParameter().getParameterizedType();
        if (!(paramType instanceof ParameterizedType)) {
            return false;
        }

        // Get the actual type argument (e.g., SomeDao from Page<SomeDao>)
        Type[] typeArgs = ((ParameterizedType) paramType).getActualTypeArguments();
        if (typeArgs.length == 0) {
            return false;
        }

        // Check if the type argument is a Class and if it's BaseDao or a subclass
        Type genericType = typeArgs[0];
        if (genericType instanceof Class<?>) {
            return BaseDao.class.isAssignableFrom((Class<?>) genericType);
        }

        return false;
    }

    @Override
    public @Nullable Object resolveParameter(ParameterContext parameterContext, @NonNull ExtensionContext extensionContext) throws ParameterResolutionException {
        PageImplObj annotation = parameterContext.findAnnotation(PageImplObj.class).
                orElseThrow(() -> new ParameterResolutionException("Missing @PageImplObj annotation"));
        int page = annotation.page();
        int size = annotation.size();
        int totalElements = annotation.totalElements();

        @SuppressWarnings("unchecked")
        Class<BaseDao> elementType =
                parameterContext.getParameter().getType().getTypeParameters().length > 0
                        ? (Class<BaseDao>) ((ParameterizedType) parameterContext
                        .getParameter()
                        .getParameterizedType())
                        .getActualTypeArguments()[0]
                        : BaseDao.class;

        // Use the generic method to get the list
        List<BaseDao> list = getValidListForType(elementType, totalElements);

        // Create and return the PageImpl
        return new PageImpl<>(list, PageRequest.of(page, size), totalElements);
    }

}
