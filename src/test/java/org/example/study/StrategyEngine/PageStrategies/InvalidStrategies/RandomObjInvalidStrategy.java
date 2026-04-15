package org.example.study.StrategyEngine.PageStrategies.InvalidStrategies;

import org.example.study.StrategyEngine.FieldInvalidators.Services.FieldInvalidationService;
import org.example.study.StrategyEngine.interfaces.InvalidDTOGenerationStrategy;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

import static org.example.study.testData.TestData.getSingleValidForType;
import static org.example.study.testData.TestData.getValidListForType;

public class RandomObjInvalidStrategy implements InvalidDTOGenerationStrategy {

    private final FieldInvalidationService service;

    public RandomObjInvalidStrategy(FieldInvalidationService service) {
        this.service = service;
    }

    @Override
    public Object generateInvalidObj(Class<?> clazz, Field field, Class<? extends Annotation> annotationToBreak) throws IllegalAccessException {
        Object obj = getSingleValidForType(clazz);
        service.invalidateField(obj, field, annotationToBreak);
        return obj;
    }

    @Override
    public List<?> generateInvalidList(Class<?> clazz, int count) throws IllegalAccessException {
        List<?> list = getValidListForType(clazz, count);
        for (Object o : list) {
            service.invalidateRandomField(o);
        }
        return list;
    }
}


