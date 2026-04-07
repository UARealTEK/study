package org.example.study.StrategyEngine.DTOStrategies;

import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.example.study.Annotations.NoConstraint;
import org.example.study.StrategyEngine.FieldInvalidators.BlankInvalidator;
import org.example.study.StrategyEngine.FieldInvalidators.MaxInvalidator;
import org.example.study.StrategyEngine.FieldInvalidators.MinInvalidator;
import org.example.study.StrategyEngine.FieldInvalidators.SizeInvalidator;
import org.example.study.StrategyEngine.interfaces.FieldInvalidator;
import org.example.study.StrategyEngine.interfaces.InvalidDTOGenerationStrategy;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import static org.example.study.testData.TestData.getSingleValidForType;
import static org.example.study.testData.TestData.getValidListForType;

/**
 * Generates invalid DTOs by violating constraint annotations on specific fields.
 * 
 * This strategy creates objects with all constraints satisfied, then invalidates
 * specific fields by breaking their constraint annotations. Supports both single
 * object and list generation with optional random field invalidation.
 */
public class GenericDtoInvalidStrategy implements InvalidDTOGenerationStrategy {

    @Override
    public Object generate(Class<?> clazz, Field field, Class<? extends Annotation> annotation) throws IllegalAccessException, NoSuchFieldException {
        Object dao = getSingleValidForType(clazz);
        invalidateField(dao, field, annotation);
        return dao;
    }

    @Override
    public List<?> generateList(Class<?> clazz, int count) throws IllegalAccessException {
        List<?> list = getValidListForType(clazz, count);
        for (Object o : list) {
            invalidateRandomField(o);
        }
        return list;
    }

    private <T> void invalidateField(T obj, Field field, Class<? extends Annotation> annotation) throws IllegalAccessException {
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

            FieldInvalidator invalidator = fieldInvalidators.get(randomAnnotation.annotationType());
            if (invalidator == null) {
                throw new IllegalStateException("No invalidator found for field " + randomAnnotation);
            }
            invalidator.invalidate(obj,field);
            return;
        }

        FieldInvalidator fieldInvalidator = fieldInvalidators.get(annotation);
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
                .filter(a -> fieldInvalidators.containsKey(a.annotationType()))
                .toList();
    }

    /**
        * Map of supported constraint annotations to their corresponding invalidators.
         * This allows for easy extension by simply adding new annotations and their invalidators to the map.
     *
     * Currently, supports Min, Max, NotBlank, and Size annotations. ID fields are explicitly excluded from invalidation.
     */
    private final Map<Class<? extends Annotation>, FieldInvalidator> fieldInvalidators = Map.of(
            Min.class, new MinInvalidator(),
            Max.class, new MaxInvalidator(),
            NotBlank.class, new BlankInvalidator(),
            Size.class, new SizeInvalidator()
    );

}
