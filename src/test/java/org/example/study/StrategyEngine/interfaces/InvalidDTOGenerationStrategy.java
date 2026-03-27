package org.example.study.StrategyEngine.interfaces;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import org.example.study.DTOs.BaseUser;

import java.lang.reflect.Field;

//TODO: think to make it fully generic
public interface InvalidDTOGenerationStrategy {
    <T extends BaseUser> T generate(Class<T> clazz) throws NoSuchFieldException;

    default <T extends BaseUser> T invalidateField(T obj, Field field) {

        if (field.isAnnotationPresent(Min.class) && field.getName().equals("age")) {
            long minValue = field.getAnnotation(Min.class).value();

            obj.setAge((int) (minValue - 1)); // Set to a value less than the minimum
            return obj;
        } else if (field.isAnnotationPresent(Max.class) && field.getName().equals("age")) {
            long maxValue = field.getAnnotation(Max.class).value();

            obj.setAge((int) (maxValue + 1)); // Set to a value greater than the maximum
            return obj;
        } else if (field.isAnnotationPresent(Size.class) && field.getName().equals("fullName")) {
            int minVal = field.getAnnotation(Size.class).min();
            if (obj.getFullName().length() >= minVal) {
                obj.setFullName(obj.getFullName().substring(0, minVal - 1)); // Set to a string shorter than the minimum
            } else obj.setFullName(""); // Set to an empty string to invalidate
            return obj;
        } else {
            obj.setGender(null);
            return obj;
        }
    }
}
