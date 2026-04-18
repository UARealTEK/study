package org.example.study.StrategyEngine.PageStrategies.InvalidStrategies;

import org.example.study.StrategyEngine.FieldInvalidators.Services.FieldInvalidationService;
import org.example.study.StrategyEngine.interfaces.InvalidDTOGenerationStrategy;

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
public class SameObjInvalidStrategy implements InvalidDTOGenerationStrategy {

    private final FieldInvalidationService service;

    public SameObjInvalidStrategy(FieldInvalidationService service) {
        this.service = service;
    }

    @Override
    public <T> T generateInvalidObj(Class<T> clazz, Field field, Class<? extends Annotation> annotationToBreak) throws IllegalAccessException {
        T dao = getSingleValidForType(clazz);
        service.invalidateField(dao, field, annotationToBreak);
        return dao;
    }

    @Override
    public <T> List<T> generateInvalidList(Class<T> clazz, int count) throws IllegalAccessException {
        List<T> list = getValidListForType(clazz, count);
        for (Object o : list) {
            service.invalidateRandomField(o);
        }
        return Stream.generate(list::getFirst).limit(count).toList();
    }
}


