package org.example.study.StrategyEngine.FieldInvalidators;

import org.example.study.StrategyEngine.interfaces.FieldInvalidator;

import java.lang.reflect.Field;

public class NotNullInvalidator implements FieldInvalidator {

    @Override
    public void invalidate(Object obj, Field field) throws IllegalAccessException {
        field.setAccessible(true);

            field.set(obj, null);
    }
}
