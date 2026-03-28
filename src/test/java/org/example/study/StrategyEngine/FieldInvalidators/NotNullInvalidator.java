package org.example.study.StrategyEngine.FieldInvalidators;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import org.example.study.StrategyEngine.interfaces.FieldInvalidator;

import java.lang.reflect.Field;

public class NotNullInvalidator implements FieldInvalidator {

    @Override
    public boolean supports(Field field) {
        return field.isAnnotationPresent(NotNull.class);
    }

    @Override
    public void invalidate(Object obj, Field field) throws IllegalAccessException {
        field.setAccessible(true);

            field.set(obj, null);
    }
}
