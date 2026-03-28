package org.example.study.StrategyEngine.FieldInvalidators;

import jakarta.validation.constraints.Min;
import org.example.study.StrategyEngine.interfaces.FieldInvalidator;

import java.lang.reflect.Field;

public class MinInvalidator implements FieldInvalidator {

    @Override
    public boolean supports(Field field) {
        return field.isAnnotationPresent(Min.class);
    }

    @Override
    public void invalidate(Object obj, Field field) {
        field.setAccessible(true);
        long val = field.getAnnotation(Min.class).value();

        try {
            field.set(obj, (int) (val -1));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
