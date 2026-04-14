package org.example.study.StrategyEngine.FieldInvalidators.Services;

import jakarta.persistence.Id;
import org.example.study.Annotations.NoConstraint;
import org.example.study.StrategyEngine.FieldInvalidators.Factories.FieldInvalidatorRegistry;
import org.example.study.StrategyEngine.interfaces.FieldInvalidator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Service that uses internal FieldInvalidatorRegistry to invalidate a given field for the given object
 * based on the received annotation
 */

//TODO: make it work and refactor the structure so it fits my resolvers
public class FieldInvalidationService {

    private FieldInvalidatorRegistry fieldInvalidatorRegistry;

    FieldInvalidationService(FieldInvalidatorRegistry fieldInvalidatorRegistry) {
        this.fieldInvalidatorRegistry = fieldInvalidatorRegistry;
    }

    public <T> void invalidateField(T obj, Field field, Class<? extends Annotation> annotation) throws IllegalAccessException {
        if (field.isAnnotationPresent(Id.class)) {
            throw new IllegalArgumentException("Cannot invalidate an ID field.");
        }
        field.setAccessible(true);
        if (!isFieldTypeCompatible(field, obj)) {
            throw new IllegalArgumentException("Field type does not match the generated object's type.");
        }
        if (annotation == NoConstraint.class) {
            List<? extends Annotation> annotations = getMatchedAnnotations(field);
            if (annotations.isEmpty()) {
                throw new IllegalStateException("No constraints found on the field to invalidate.");
            }

            Annotation randomAnnotation = annotations.get(ThreadLocalRandom.current().nextInt(annotations.size()));

            FieldInvalidator invalidator = fieldInvalidatorRegistry.getInvalidator(randomAnnotation.annotationType());
            if (invalidator == null) {
                throw new IllegalStateException("No invalidator found for field annotation " + randomAnnotation);
            }
            invalidator.invalidate(obj,field);
            return;
        }

        FieldInvalidator fieldInvalidator = fieldInvalidatorRegistry.getInvalidator(annotation);
        if (fieldInvalidator == null) {
            throw new IllegalArgumentException(
                    "No invalidator found for annotation: " + annotation
            );
        }

        fieldInvalidator.invalidate(obj, field);
    }

    private <T> void invalidateRandomField(T obj) throws IllegalAccessException {
        List<Field> fields = Arrays.stream(obj.getClass().getDeclaredFields())
                .filter(f -> f.getAnnotations().length > 0)
                .filter(f -> !f.isAnnotationPresent(Id.class))
                .toList();
        if (fields.isEmpty()) {
            throw new IllegalStateException("No fields found in the object to invalidate.");
        }

        Field randomField = fields.get(ThreadLocalRandom.current().nextInt(fields.size()));
        randomField.setAccessible(true);
        List<? extends Annotation> annotations = getMatchedAnnotations(randomField);

        if (annotations.isEmpty()) {
            throw new IllegalStateException("No constraints found on the field to invalidate.");
        }

        Annotation randomAnnotation = annotations.get(ThreadLocalRandom.current().nextInt(annotations.size()));
        invalidateField(obj, randomField, randomAnnotation.annotationType());
    }

    private boolean isFieldTypeCompatible(Field field, Object value) {
        return field.getDeclaringClass().isAssignableFrom(value.getClass());
    }

    private List<? extends Annotation> getMatchedAnnotations(Field field) {
        return Arrays.stream(field.getAnnotations())
                .filter(a -> fieldInvalidatorRegistry.getAllInvalidators().containsKey(a.annotationType()))
                .toList();
    }
}
