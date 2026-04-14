package org.example.study.StrategyEngine.PageStrategies;

import org.example.study.StrategyEngine.interfaces.InvalidDTOGenerationStrategy;
import org.example.study.StrategyEngine.interfaces.ValidDTOGenerationStrategy;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

/**
 * Generates empty lists (no objects).
 */
public class EmptyStrategy implements ValidDTOGenerationStrategy, InvalidDTOGenerationStrategy {

    @Override
    public <T> List<T> generateValidList(Class<T> clazz, int count) {
        return List.of();
    }

    @Override
    public Object generateInvalidObj(Class<?> clazz, Field field, Class<? extends Annotation> annotationToBreak) throws NoSuchFieldException, IllegalAccessException {
        throw new UnsupportedOperationException("EmptyStrategy does not support generating invalid objects.");
    }

    @Override
    public List<?> generateInvalidList(Class<?> clazz, int count) throws NoSuchFieldException, IllegalAccessException {
        throw new UnsupportedOperationException("EmptyStrategy does not support generating invalid objects.");
    }

}
