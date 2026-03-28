package org.example.study.StrategyEngine.FieldInvalidators;

import org.example.study.StrategyEngine.interfaces.FieldInvalidator;
import org.example.study.util.Exceptions.CustomExceptions.InvalidConstraintConfigurationException;

import java.lang.reflect.Field;

public class BlankInvalidator implements FieldInvalidator {

    @Override
    public void invalidate(Object obj, Field field) {
        field.setAccessible(true);

        try {
            if (field.getType().equals(String.class)) {
                field.set(obj, "");
            } else {
                throw new InvalidConstraintConfigurationException(
                        "@NotBlank not supported for type: " + field.getType()
                );
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to set field: " + field.getName(), e);
        }
    }
}
