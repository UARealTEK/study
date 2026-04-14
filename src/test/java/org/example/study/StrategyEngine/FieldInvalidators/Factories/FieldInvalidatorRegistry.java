package org.example.study.StrategyEngine.FieldInvalidators.Factories;

import jakarta.validation.constraints.*;
import org.example.study.StrategyEngine.FieldInvalidators.*;
import org.example.study.StrategyEngine.interfaces.FieldInvalidator;

import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * Invalidator registry which produces available Invalidators
 */
public class FieldInvalidatorRegistry {

    private Map<Class<? extends Annotation>, FieldInvalidator> fieldInvalidators;

    FieldInvalidatorRegistry() {
        this.fieldInvalidators = Map.of(
                Min.class, new MinInvalidator(),
                Max.class, new MaxInvalidator(),
                NotBlank.class, new BlankInvalidator(),
                Size.class, new SizeInvalidator(),
                NotNull.class, new NotNullInvalidator()
        );
    }

    public FieldInvalidator getInvalidator(Class<? extends Annotation> annotation) {
        return fieldInvalidators.get(annotation);
    }

    public Map<Class<? extends Annotation>, FieldInvalidator> getAllInvalidators() {
        return fieldInvalidators;
    }
}
