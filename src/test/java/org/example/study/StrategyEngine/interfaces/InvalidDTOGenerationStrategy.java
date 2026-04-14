package org.example.study.StrategyEngine.interfaces;

import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import org.example.study.Annotations.NoConstraint;
import org.example.study.StrategyEngine.FieldInvalidators.*;
import org.example.study.StrategyEngine.PageStrategies.EmptyStrategy;
import org.example.study.StrategyEngine.PageStrategies.RandomStrategy;
import org.example.study.StrategyEngine.PageStrategies.SameObjStrategy;
import org.example.study.enums.PageStrategyType;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

// This logic may be placed inside Each concrete strategy with each own implementation
// but for now - separate interface which is further injected in OTHER required places and reused -> fine

/**
 * Generic strategy interface for generating invalid DTOs.
 * Implementations can create invalid instances by violating specific constraints on fields.
 * */
public interface InvalidDTOGenerationStrategy {

    /**
     * Generates a single invalid DTO instance by violating a specific constraint annotation on a field.
     * This method is used to create an object that fails validation for testing purposes.
     *
     * @param clazz the target class to instantiate and modify
     * @param field the field on which the constraint will be violated
     * @param annotationToBreak the validation annotation class to break (e.g., Min.class, NotNull.class, Pattern.class)
     * @return an instance of the specified class with an invalid field value
     * @throws NoSuchFieldException if the field cannot be found on the class
     * @throws IllegalAccessException if the field cannot be accessed for modification
     */
    Object generateInvalidObj(Class<?> clazz, Field field, Class<? extends Annotation> annotationToBreak) throws NoSuchFieldException, IllegalAccessException;

    /**
     * Generates a list of invalid DTO instances.
     * This method creates multiple objects that fail validation for testing purposes.
     * The exact behavior depends on the implementation strategy (e.g., random values, empty values, boundary values).
     *
     * @param clazz the target class to instantiate and modify
     * @param count the number of invalid instances to generate
     * @return a list containing {@code count} invalid instances of the specified class
     * @throws NoSuchFieldException if required fields cannot be found on the class
     * @throws IllegalAccessException if fields cannot be accessed for modification
     */
    List<?> generateInvalidList(Class<?> clazz, int count) throws NoSuchFieldException, IllegalAccessException;

}
