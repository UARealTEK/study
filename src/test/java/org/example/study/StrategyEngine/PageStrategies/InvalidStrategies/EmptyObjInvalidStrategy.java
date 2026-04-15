package org.example.study.StrategyEngine.PageStrategies.InvalidStrategies;

import org.example.study.StrategyEngine.interfaces.InvalidDTOGenerationStrategy;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

/**
 * Generates empty lists (no objects).
 */
public class EmptyObjInvalidStrategy implements InvalidDTOGenerationStrategy {

    @Override
    public Object generateInvalidObj(Class<?> clazz, Field field, Class<? extends Annotation> annotationToBreak) throws NoSuchFieldException, IllegalAccessException {
        throw new UnsupportedOperationException("EmptyStrategy does not support generating invalid objects.");
    }

    @Override
    public List<?> generateInvalidList(Class<?> clazz, int count) throws NoSuchFieldException, IllegalAccessException {
        throw new UnsupportedOperationException("EmptyStrategy does not support generating invalid objects.");
    }

}
