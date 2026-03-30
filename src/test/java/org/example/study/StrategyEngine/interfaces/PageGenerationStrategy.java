package org.example.study.StrategyEngine.interfaces;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

//TODO: move GenericDtoInvalidStrategy in here.
// which means -> PageGenerationStrategy will handle both valid and Invalid generations
public interface PageGenerationStrategy {
    <T> List<T> generate(Class<T> clazz, int count);
    Object generateInvalidObj(Class<?> clazz, Field field, Class<? extends Annotation> annotationToBreak) throws NoSuchFieldException, IllegalAccessException;
    List<?> generateInvalidObjList(Class<?> clazz, int count) throws NoSuchFieldException, IllegalAccessException;
}
