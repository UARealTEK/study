package org.example.study.StrategyEngine.DTOStrategies;

import org.example.study.StrategyEngine.interfaces.FieldInvalidator;
import org.example.study.StrategyEngine.interfaces.InvalidDTOGenerationStrategy;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.example.study.testData.TestData.getSingleValidForType;

//TODO: Test it, complete it
public class GenericDtoInvalidStrategy implements InvalidDTOGenerationStrategy {


    @Override
    public <T> T generate(Class<T> clazz, Field field) throws IllegalAccessException {
        Annotation[] annotations = field.getAnnotations();
        T dao = getSingleValidForType(clazz);
        invalidateField(dao, field, annotations);
        return dao;
    }

    private <T> Field getSpecificField(T obj, Field field) {
        Field[] fields = getAllFields(obj.getClass());
        return Arrays.stream(fields).filter(i -> i.getName().equals(field.getName())).findFirst().orElse(null);
    }

    private <T> void invalidateField(T obj, Field field, Annotation... annotations) throws IllegalAccessException {
        Field currentField = getSpecificField(obj, field);
        for (Annotation annotation : annotations) {
            FieldInvalidator fieldInvalidator = fieldInvalidators.get(annotation.annotationType());

            if (fieldInvalidator == null) {
                throw new RuntimeException(
                        "No invalidator found for annotation: " + annotation.annotationType()
                );
            }
            fieldInvalidator.invalidate(obj, currentField);
        }
    }

    private Field[] getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();

        while (clazz != null && !clazz.getSuperclass().equals(Object.class)) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }

        return fields.toArray(new Field[0]);
    }

}
