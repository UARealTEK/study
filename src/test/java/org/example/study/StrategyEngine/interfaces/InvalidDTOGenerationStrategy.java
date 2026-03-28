package org.example.study.StrategyEngine.interfaces;

import java.lang.reflect.Field;
//TODO: This invalidates fields only for BaseUser.class DTOs. how do I make it fully compatible?
// refactoring this into FieldInvalidator engine

/**
 * General Idea - invalidate a SPECIFIC field in the DTO judging by the Constraint annotation that is set upon that field
 */
public interface InvalidDTOGenerationStrategy {

    <T> T generate(Class<T> clazz, Field field) throws NoSuchFieldException, IllegalAccessException;
}
