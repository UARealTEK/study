package org.example.study.StrategyEngine.PageStrategies;

import org.example.study.StrategyEngine.interfaces.ValidDTOGenerationStrategy;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

/**
 * Generates empty lists (no objects).
 */
public class EmptyStrategy implements ValidDTOGenerationStrategy {

    @Override
    public <T> List<T> generateList(Class<T> clazz, int count) {
        return List.of();
    }

    /**
     * This method is not supported in EmptyStrategy since it generates empty lists and does not create any objects.
     * @return UnsupportedOperationException indicating that invalid object generation is not supported.
     */
    @Override
    public Object generateInvalidObj(Class<?> clazz, Field field, Class<? extends Annotation> annotationToBreak) {
        throw new UnsupportedOperationException("EmptyStrategy does not support generating invalid objects.");
    }

    /**
     * This method is not supported in EmptyStrategy since it generates empty lists and does not create any objects.
     * @return UnsupportedOperationException indicating that invalid object generation is not supported.
     */
    @Override
    public List<?> generateInvalidObjList(Class<?> clazz, int count) {
        throw new UnsupportedOperationException("EmptyStrategy does not support generating lists of invalid objects.");
    }
}
