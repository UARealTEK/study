package org.example.study.StrategyEngine.interfaces;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * General Idea - invalidate a SPECIFIC field in the DTO judging by the Constraint annotation that is set upon that field
 */
public interface InvalidDTOGenerationStrategy {
    <T, U extends Annotation> T generate(Class<T> clazz, Field field, Class<U> annotationToBreak) throws NoSuchFieldException, IllegalAccessException;
}
