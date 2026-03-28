package org.example.study.StrategyEngine.FieldInvalidators;

import jakarta.validation.constraints.Size;
import org.example.study.StrategyEngine.interfaces.FieldInvalidator;
import org.example.study.util.Exceptions.CustomExceptions.InvalidConstraintConfigurationException;

import java.lang.reflect.Field;
import java.util.Collections;

public class SizeInvalidator implements FieldInvalidator {

    @Override
    public boolean supports(Field field) {
        return field.isAnnotationPresent(Size.class);
    }

    @Override
    public void invalidate(Object obj, Field field) {
        field.setAccessible(true);

        Size size = field.getAnnotation(Size.class);
        int min = size.min();
        int max = size.max();

        if (min < 0 || max < 0 || min > max) {
            throw new InvalidConstraintConfigurationException(
                    "@Size invalid on field '" + field.getName() +
                            "': min=" + min + ", max=" + max
            );
        }

        try {
            if (field.getType().equals(String.class)) {
                field.set(obj, generateString(min - 1));
            } else {
                throw new InvalidConstraintConfigurationException(
                        "@Size not supported for type: " + field.getType()
                );
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to set field: " + field.getName(), e);
        }
    }

    private String generateString(int length) {
        if (length < 1)  {
            return "";
        }

        return String.join("", Collections.nCopies(length, "_"));
    }


}
