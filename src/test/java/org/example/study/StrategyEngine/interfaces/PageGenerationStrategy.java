package org.example.study.StrategyEngine.interfaces;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

/**
 * Strategy for generating valid and invalid DTOs.
 * 
 * Valid generation creates instances with all constraints satisfied.
 * Invalid generation creates instances with specific constraints violated.
 */
public interface PageGenerationStrategy {
    /**
     * Generate a list of valid objects of the specified class.
     * @param clazz the class to generate instances of
     * @param count number of instances to generate
     * @return list of valid instances
     */
    <T> List<T> generate(Class<T> clazz, int count);

    /**
     * Generate a single invalid object with a specific field violating the given constraint.
     * @param clazz the class to generate an instance of
     * @param field the field to invalidate
     * @param annotationToBreak the annotation constraint to violate
     * @return an invalid instance
     * @throws NoSuchFieldException if the field is not found
     * @throws IllegalAccessException if the field cannot be accessed
     */
    Object generateInvalidObj(Class<?> clazz, Field field, Class<? extends Annotation> annotationToBreak) throws NoSuchFieldException, IllegalAccessException;

    /**
     * Generate a list of invalid objects with random constraint violations.
     * @param clazz the class to generate instances of
     * @param count number of instances to generate
     * @return list of invalid instances
     * @throws NoSuchFieldException if a field is not found
     * @throws IllegalAccessException if a field cannot be accessed
     */
    List<?> generateInvalidObjList(Class<?> clazz, int count) throws NoSuchFieldException, IllegalAccessException;
}
