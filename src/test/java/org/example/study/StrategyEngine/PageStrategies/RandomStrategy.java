package org.example.study.StrategyEngine.PageStrategies;

import org.example.study.StrategyEngine.FieldInvalidators.Services.FieldInvalidationService;
import org.example.study.StrategyEngine.interfaces.InvalidDTOGenerationStrategy;
import org.example.study.StrategyEngine.interfaces.ValidDTOGenerationStrategy;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

import static org.example.study.testData.TestData.getValidListForType;

//TODO: should generate data and then use InvalidationService to invalidate a specific given field
public class RandomStrategy implements ValidDTOGenerationStrategy, InvalidDTOGenerationStrategy {

    private FieldInvalidationService service;

    @Override
    public <T> List<T> generateValidList(Class<T> clazz, int count) {
        return getValidListForType(clazz, 10);
    }

    @Override
    public Object generateInvalidObj(Class<?> clazz, Field field, Class<? extends Annotation> annotationToBreak) throws NoSuchFieldException, IllegalAccessException {
        return null;
    }

    @Override
    public List<?> generateInvalidList(Class<?> clazz, int count) throws NoSuchFieldException, IllegalAccessException {
        return null;
    }
}


