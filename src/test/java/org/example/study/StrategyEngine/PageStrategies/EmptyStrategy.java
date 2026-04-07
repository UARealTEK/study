package org.example.study.StrategyEngine.PageStrategies;

import org.example.study.StrategyEngine.DTOStrategies.GenericDtoInvalidStrategy;
import org.example.study.StrategyEngine.interfaces.PageGenerationStrategy;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

/**
 * Generates empty lists (no objects).
 * Delegates invalid generation to GenericDtoInvalidStrategy.
 */
public class EmptyStrategy implements PageGenerationStrategy {

    private static final GenericDtoInvalidStrategy invalidStrategy = new GenericDtoInvalidStrategy();

    @Override
    public <T> List<T> generate(Class<T> clazz, int count) {
        return List.of();
    }

    @Override
    public Object generateInvalidObj(Class<?> clazz, Field field, Class<? extends Annotation> annotationToBreak) throws NoSuchFieldException, IllegalAccessException {
        return invalidStrategy.generate(clazz, field, annotationToBreak);
    }

    @Override
    public List<?> generateInvalidObjList(Class<?> clazz, int count) throws NoSuchFieldException, IllegalAccessException {
        return invalidStrategy.generateList(clazz, count);
    }
}


