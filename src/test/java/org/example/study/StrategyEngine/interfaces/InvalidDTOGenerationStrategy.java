package org.example.study.StrategyEngine.interfaces;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

/**
 * General Idea - invalidate a SPECIFIC field in the DTO judging by the Constraint annotation that is set upon that field
 */
public interface InvalidDTOGenerationStrategy {
    Object generate(Class<?> clazz, Field field, Class<? extends Annotation> annotationToBreak) throws NoSuchFieldException, IllegalAccessException;
    List<?> generateList(Class<?> clazz, int count) throws NoSuchFieldException, IllegalAccessException;
}
