package org.example.study.StrategyEngine.PageStrategies;

import org.example.study.StrategyEngine.DTOStrategies.GenericDtoInvalidStrategy;
import org.example.study.StrategyEngine.interfaces.ValidDTOGenerationStrategy;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

import static org.example.study.testData.TestData.getValidListForType;

/**
 * Generates lists of random valid objects.
 * Delegates invalid generation to GenericDtoInvalidStrategy.
 */
public class RandomStrategy implements ValidDTOGenerationStrategy {

    private static final GenericDtoInvalidStrategy invalidStrategy = new GenericDtoInvalidStrategy();

    @Override
    public <T> List<T> generateList(Class<T> clazz, int count) {
        return getValidListForType(clazz, 10);
    }

    @Override
    public Object generateInvalidObj(Class<?> clazz, Field field, Class<? extends Annotation> annotationToBreak) throws NoSuchFieldException, IllegalAccessException {
        return invalidStrategy.generate(clazz, field, annotationToBreak);
    }

    @Override
    public List<?> generateInvalidObjList(Class<?> clazz, int count) throws IllegalAccessException {
        return invalidStrategy.generateList(clazz, count);
    }
}


