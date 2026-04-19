package org.example.study.StrategyEngine.PageStrategies.InvalidStrategies;

import org.example.study.StrategyEngine.interfaces.InvalidDTOGenerationStrategy;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.util.List;

/**
 * Generates empty lists (no objects).
 */
public class EmptyObjInvalidStrategy implements InvalidDTOGenerationStrategy {

    @Override
    public <T> T generateInvalidObj(Class<T> clazz, Field field, Class<? extends Annotation> annotationToBreak) {
        throw new UnsupportedOperationException("EmptyStrategy does not support generating invalid objects.");
    }

    @Override
    public <T> List<T> generateInvalidList(Class<T> clazz, int count) {
        throw new UnsupportedOperationException("EmptyStrategy does not support generating invalid objects.");
    }

}
