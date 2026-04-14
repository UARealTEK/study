package org.example.study.StrategyEngine.PageStrategies;

import org.example.study.StrategyEngine.FieldInvalidators.Services.FieldInvalidationService;
import org.example.study.StrategyEngine.interfaces.InvalidDTOGenerationStrategy;
import org.example.study.StrategyEngine.interfaces.ValidDTOGenerationStrategy;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Stream;

import static org.example.study.testData.TestData.getSingleValidForType;
import static org.example.study.testData.TestData.getValidListForType;

/**
 * Generates lists with the same valid object repeated.
 * Delegates invalid generation to GenericDtoInvalidStrategy.
 */
public class SameObjStrategy implements ValidDTOGenerationStrategy, InvalidDTOGenerationStrategy {

    private final FieldInvalidationService service;

    public SameObjStrategy(FieldInvalidationService service) {
        this.service = service;
    }

    @Override
    public <T> List<T> generateValidList(Class<T> clazz, int count) {
        List<T> list = getValidListForType(clazz, 1);
        return Stream.generate(list::getFirst).limit(count).toList();
    }

    @Override
    public Object generateInvalidObj(Class<?> clazz, Field field, Class<? extends Annotation> annotationToBreak) throws NoSuchFieldException, IllegalAccessException {
        Object dao = getSingleValidForType(clazz);
        service.invalidateField(dao, field, annotationToBreak);
        return dao;
    }

    @Override
    public List<?> generateInvalidList(Class<?> clazz, int count) throws NoSuchFieldException, IllegalAccessException {
        List<?> list = getValidListForType(clazz, count);
        for (Object o : list) {
            service.invalidateRandomField(o);
        }
        return Stream.generate(list::getFirst).limit(count).toList();
    }
}


