package org.example.study.StrategyEngine.PageStrategies;

import org.example.study.StrategyEngine.DTOStrategies.GenericDtoInvalidStrategy;
import org.example.study.StrategyEngine.interfaces.InvalidDTOGenerationStrategy;
import org.example.study.StrategyEngine.interfaces.ValidDTOGenerationStrategy;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

import static org.example.study.testData.TestData.getValidListForType;

/**
 * Generates lists of random valid objects.
 * Delegates invalid generation to GenericDtoInvalidStrategy.
 */
public class RandomStrategy implements ValidDTOGenerationStrategy, InvalidDTOGenerationStrategy {

    private static final GenericDtoInvalidStrategy invalidStrategy = new GenericDtoInvalidStrategy();

    @Override
    public <T> List<T> generateValidList(Class<T> clazz, int count) {
        return getValidListForType(clazz, 10);
    }

    @Override
    public Object generateInvalidObj(Class<?> clazz, Field field, Class<? extends Annotation> annotationToBreak) throws NoSuchFieldException, IllegalAccessException {
        return invalidStrategy.generateInvalidObj(clazz, field, annotationToBreak);
    }

    @Override
    public List<?> generateInvalidList(Class<?> clazz, int count) throws NoSuchFieldException, IllegalAccessException {
        return invalidStrategy.generateInvalidList(clazz, count);
    }
}


