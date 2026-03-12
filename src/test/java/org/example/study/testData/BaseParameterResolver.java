package org.example.study.testData;

import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

@SuppressWarnings("BooleanMethodIsAlwaysInverted")
public abstract class BaseParameterResolver implements ParameterResolver {

    protected static boolean isAnnotatedWith(ParameterContext context, Class<? extends Annotation> annotation) {
        return context.isAnnotated(annotation);
    }

    protected boolean isSuperOf(Class<?> clazz, ParameterContext context) {
        return clazz.isAssignableFrom(context.getParameter().getType());
    }

    protected boolean hasParametrizedType(ParameterContext context) {
        return context.getParameter().getParameterizedType() instanceof ParameterizedType;
    }

    @SuppressWarnings("SameParameterValue")
    protected boolean isParametrizedTypeOf(Class<?> clazz, ParameterContext context) {
        Type paramType = context.getParameter().getParameterizedType();

        if (!(paramType instanceof ParameterizedType)) {
            return false;
        }

        Type genericType = ((ParameterizedType) paramType).getActualTypeArguments()[0];
        if (genericType instanceof Class<?> generic) {
            return clazz.isAssignableFrom(generic);
        } else throw new IllegalArgumentException("Type argument is not a Class");
    }

    protected Class<?> getGenericType(ParameterContext parameterContext) {
        Type paramType = parameterContext.getParameter().getParameterizedType();
        if (paramType instanceof ParameterizedType) {
            Type[] typeArgs = ((ParameterizedType) paramType).getActualTypeArguments();
            if (typeArgs.length > 0 && typeArgs[0] instanceof Class<?> clazz) {
                return clazz;
            }
        }
        throw new ParameterResolutionException("Unable to determine generic type for parameter: " + parameterContext.getParameter().getName());
    }
}
