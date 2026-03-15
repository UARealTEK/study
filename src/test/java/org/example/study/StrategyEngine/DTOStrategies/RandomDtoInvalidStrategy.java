package org.example.study.StrategyEngine.DTOStrategies;

import org.example.study.DTOs.BaseUser;
import org.example.study.StrategyEngine.interfaces.InvalidDTOGenerationStrategy;

import java.lang.reflect.Field;
import java.util.concurrent.ThreadLocalRandom;

import static org.example.study.testData.TestData.getSingleValidForType;

//TODO: invalidate fields based on existing constraints
// for now - Im hardcoding it. But would be nice to invalidate them based on existing field constraints
public class RandomDtoInvalidStrategy<T extends BaseUser> implements InvalidDTOGenerationStrategy<T> {


    @Override
    public T generate(Class<T> clazz) {
        T dao = getSingleValidForType(clazz);
        return setSelectedFieldValue(dao);
    }

    private Field getRandomField(T obj) {
        //TODO: figure out why I really call .getSuperClass() to get all fields in the class hierarchy
        Field[] fields = obj.getClass().getSuperclass().getDeclaredFields();
        return fields[ThreadLocalRandom.current().nextInt(fields.length)];
    }

    private T setSelectedFieldValue(T obj) {
        Field field = getRandomField(obj);
        String fieldName = field.getName();
        switch (fieldName) {
            case "age" -> obj.setAge(0);
            case "fullName" -> obj.setFullName("");
            case "gender" -> obj.setGender(null);
            default -> throw new IllegalArgumentException("Unexpected field: " + fieldName);
        }
        return obj;
    }

}
