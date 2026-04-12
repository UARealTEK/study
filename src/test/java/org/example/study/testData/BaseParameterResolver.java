package org.example.study.testData;

import org.example.study.StrategyEngine.PageStrategies.EmptyStrategy;
import org.example.study.StrategyEngine.PageStrategies.RandomStrategy;
import org.example.study.StrategyEngine.PageStrategies.SameObjStrategy;
import org.example.study.StrategyEngine.interfaces.PageGenerationStrategy;
import org.example.study.enums.PageStrategyType;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

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

    /**
     * Finds a field by name, walking up the class hierarchy to support inherited fields.*
     * This is necessary because UserDto extends BaseUser, and fields like 'age', 'fullName'
     * are declared in BaseUser, not UserDto directly.
     *
     * @param clazz the class to search (starting point)
     * @param fieldName the name of the field to find
     * @return the Field object
     * @throws NoSuchFieldException if the field is not found in this class or any superclass
     */
    protected Field findField(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        if (clazz == null) {
            throw new IllegalArgumentException("Class cannot be null");
        }

        Class<?> current = clazz;

        while (current != null && current != Object.class) {
            try {
                return current.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                current = current.getSuperclass();
            }
        }

        throw new NoSuchFieldException("Field '" + fieldName + "' not found in class " + clazz.getName() + " or its superclasses");
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

    protected void validateStrategyType(PageStrategyType type, int totalElements) {
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

    protected static final Map<PageStrategyType, PageGenerationStrategy> pageStrategyMap = Map.of(
            PageStrategyType.RANDOM, new RandomStrategy(),
            PageStrategyType.SAME, new SameObjStrategy(),
            PageStrategyType.EMPTY, new EmptyStrategy()
    );
}
