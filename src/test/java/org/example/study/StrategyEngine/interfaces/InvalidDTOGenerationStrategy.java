package org.example.study.StrategyEngine.interfaces;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.List;


//TODO: I need to make sure that AnnotatedElement works in this case
// Since both Field and Method implement AnnotatedElement - I need to somehow pull the field out of it and invalidate it
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
    <T> T generateInvalidObj(Class<T> clazz, AnnotatedElement annotatedElement, Class<? extends Annotation> annotationToBreak) throws NoSuchFieldException, IllegalAccessException;

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
    <T> List<T> generateInvalidList(Class<T> clazz, int count) throws NoSuchFieldException, IllegalAccessException;
}
