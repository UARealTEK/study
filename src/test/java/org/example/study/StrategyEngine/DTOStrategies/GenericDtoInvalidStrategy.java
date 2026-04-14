package org.example.study.StrategyEngine.DTOStrategies;

import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import org.example.study.Annotations.NoConstraint;
import org.example.study.StrategyEngine.FieldInvalidators.*;
import org.example.study.StrategyEngine.interfaces.FieldInvalidator;
import org.example.study.StrategyEngine.interfaces.InvalidDTOGenerationStrategy;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import static org.example.study.testData.TestData.getSingleValidForType;
import static org.example.study.testData.TestData.getValidListForType;

/**
 * Generates invalid DTOs by violating constraint annotations on specific fields.
 * This strategy creates objects with all constraints satisfied, then invalidates
 * specific fields by breaking their constraint annotations. Supports both single
 * object and list generation with optional random field invalidation.
 */
public class GenericDtoInvalidStrategy implements InvalidDTOGenerationStrategy {

    @Override
    public Object generateInvalidObj(Class<?> clazz, Field field, Class<? extends Annotation> annotation) throws IllegalAccessException, NoSuchFieldException {
        Object dao = getSingleValidForType(clazz);
        invalidateField(dao, field, annotation);
        return dao;
    }

    @Override
    public List<?> generateInvalidList(Class<?> clazz, int count) throws IllegalAccessException {
        List<?> list = getValidListForType(clazz, count);
        for (Object o : list) {
            invalidateRandomField(o);
        }
        return list;
    }
}
