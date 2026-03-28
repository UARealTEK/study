package org.example.study.StrategyEngine.interfaces;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.example.study.StrategyEngine.FieldInvalidators.BlankInvalidator;
import org.example.study.StrategyEngine.FieldInvalidators.MaxInvalidator;
import org.example.study.StrategyEngine.FieldInvalidators.MinInvalidator;
import org.example.study.StrategyEngine.FieldInvalidators.SizeInvalidator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Map;

//TODO: This invalidates fields only for BaseUser.class DTOs. how do I make it fully compatible?
// refactoring this into FieldInvalidator engine

/**
 * General Idea - invalidate a SPECIFIC field in the DTO judging by the Constraint annotation that is set upon that field
 */
public interface InvalidDTOGenerationStrategy {

    Map<Class<? extends Annotation>, FieldInvalidator> fieldInvalidators = Map.of(
            Min.class, new MinInvalidator(),
            Max.class, new MaxInvalidator(),
            NotBlank.class, new BlankInvalidator(),
            Size.class, new SizeInvalidator()
    );

    <T> T generate(Class<T> clazz, Field field) throws NoSuchFieldException, IllegalAccessException;
}
