package org.example.study.StrategyEngine.FieldInvalidators;

import jakarta.validation.constraints.Min;
import org.example.study.StrategyEngine.interfaces.FieldInvalidator;

import java.lang.reflect.Field;
import java.util.stream.Stream;

public class MinInvalidator implements FieldInvalidator {

    @Override
    public void invalidate(Object obj, Field field) {
        field.setAccessible(true);

        if (field.getType() == int.class || field.getType() == Integer.class) {
            try {
                field.set(obj, Integer.MIN_VALUE);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
