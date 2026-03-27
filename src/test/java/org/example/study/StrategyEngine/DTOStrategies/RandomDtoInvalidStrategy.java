package org.example.study.StrategyEngine.DTOStrategies;

import org.example.study.DTOs.BaseUser;
import org.example.study.StrategyEngine.interfaces.InvalidDTOGenerationStrategy;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static org.example.study.testData.TestData.getSingleValidForType;

//TODO: think to make it fully generic
public class RandomDtoInvalidStrategy implements InvalidDTOGenerationStrategy {


    @Override
    public <T extends BaseUser> T generate(Class<T> clazz) {
        T dao = getSingleValidForType(clazz);
        return setSelectedFieldValue(dao);
    }

    private <T extends BaseUser> Field getRandomField(T obj) {
        Field[] fields = getAllFields(obj.getClass());
        return fields[ThreadLocalRandom.current().nextInt(fields.length)];
    }

    private <T extends BaseUser> T setSelectedFieldValue(T obj) {
        Field field = getRandomField(obj);
        return invalidateField(obj,field);
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
