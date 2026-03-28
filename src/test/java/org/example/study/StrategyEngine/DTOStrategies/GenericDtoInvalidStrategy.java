package org.example.study.StrategyEngine.DTOStrategies;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    public <T> T generate(Class<T> clazz, Field field) throws IllegalAccessException, NoSuchFieldException {
        Field actualField = clazz.getDeclaredField(field.getName());
        Annotation[] annotations = field.getAnnotations();
        T dao = getSingleValidForType(clazz);
        invalidateField(dao, actualField, annotations);
        return dao;
    }

    private <T> void invalidateField(T obj, Field field, Annotation... annotations) throws IllegalAccessException {
        if (annotations.length == 0) {
            throw new IllegalAccessException("No annotations found");
        }
        Annotation annotation = annotations[0];
        FieldInvalidator fieldInvalidator = fieldInvalidators.get(annotation.annotationType());
        if (fieldInvalidator == null) {
            throw new IllegalArgumentException(
                    "No invalidator found for annotation: " + annotation.annotationType()
            );
        }

        field.setAccessible(true);
        fieldInvalidator.invalidate(obj, field);
    }

    //TODO: turn this into a pluggable validation engine (like Hibernate Validator)
    private final Map<Class<? extends Annotation>, FieldInvalidator> fieldInvalidators = Map.of(
            Min.class, new MinInvalidator(),
            Max.class, new MaxInvalidator(),
            NotBlank.class, new BlankInvalidator(),
            Size.class, new SizeInvalidator()
    );

}
