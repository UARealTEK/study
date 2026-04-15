package org.example.study.StrategyEngine.interfaces;

import org.jspecify.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

//TODO: Create concrete implementations for this interface
/**
 * Strategy for generating valid DTOs.
 * Valid generation creates instances with all constraints satisfied.
 */
public interface ValidDTOGenerationStrategy {
    /**
     * Generate a list of valid objects of the specified class.
     * @param clazz the class to generate instances of
     * @param count number of instances to generate
     * @return list of valid instances
     */
    <T> List<T> generateValidList(Class<T> clazz, int count);

    //TODO: probably will need generate() method to generate a single Object
}
