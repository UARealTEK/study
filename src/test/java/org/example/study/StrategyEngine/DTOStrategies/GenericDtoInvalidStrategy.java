package org.example.study.StrategyEngine.DTOStrategies;

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
import java.util.Map;

import static org.example.study.testData.TestData.getSingleValidForType;

//TODO: Test it, complete it
public class GenericDtoInvalidStrategy implements InvalidDTOGenerationStrategy {

    @Override
    public <T, U extends Annotation> T generate(Class<T> clazz, Field field, Class<U> annotation) throws IllegalAccessException, NoSuchFieldException {
        Field actualField = clazz.getDeclaredField(field.getName());
        T dao = getSingleValidForType(clazz);
        invalidateField(dao, actualField, annotation);
        return dao;
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    private <T, U extends Annotation> void invalidateField(T obj, Field field, Class<U> annotation) throws IllegalAccessException {
        field.setAccessible(true);
        if (annotation == NoConstraint.class) {
            Annotation[] annotations = field.getAnnotations();
            for (Annotation ann : annotations) {
                FieldInvalidator invalidator = fieldInvalidators.get(ann);
                if (invalidator == null) {
                    throw new IllegalStateException("No invalidator found for field " + ann);
                }
                invalidator.invalidate(obj,field);
            }
        }

        FieldInvalidator fieldInvalidator = fieldInvalidators.get(annotation);
        if (fieldInvalidator == null) {
            throw new IllegalArgumentException(
                    "No invalidator found for annotation: " + annotation
            );
        }

        fieldInvalidator.invalidate(obj, field);
    }

    private final Map<Class<? extends Annotation>, FieldInvalidator> fieldInvalidators = Map.of(
            Min.class, new MinInvalidator(),
            Max.class, new MaxInvalidator(),
            NotBlank.class, new BlankInvalidator(),
            Size.class, new SizeInvalidator()
    );

}
