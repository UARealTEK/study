package org.example.study.StrategyEngine.PageStrategies;

import org.example.study.StrategyEngine.DTOStrategies.GenericDtoInvalidStrategy;
import org.example.study.StrategyEngine.interfaces.PageGenerationStrategy;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Stream;

import static org.example.study.testData.TestData.getValidListForType;

/**
 * Generates lists with the same valid object repeated.
 * Delegates invalid generation to GenericDtoInvalidStrategy.
 */
public class SameObjStrategy implements PageGenerationStrategy {

    private static final GenericDtoInvalidStrategy invalidStrategy = new GenericDtoInvalidStrategy();

    @Override
    public <T> List<T> generate(Class<T> clazz, int count) {
        List<T> list = getValidListForType(clazz, 1);
        return Stream.generate(list::getFirst).limit(count).toList();
    }

    @Override
    public Object generateInvalidObj(Class<?> clazz, Field field, Class<? extends Annotation> annotationToBreak) throws NoSuchFieldException, IllegalAccessException {
        return invalidStrategy.generate(clazz, field, annotationToBreak);
    }

    @Override
    public List<?> generateInvalidObjList(Class<?> clazz, int count) throws IllegalAccessException {
        List<?> list = invalidStrategy.generateList(clazz, count);
        return Stream.generate(list::getFirst).limit(count).toList();
    }
}


