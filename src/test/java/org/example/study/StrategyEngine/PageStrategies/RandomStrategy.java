package org.example.study.StrategyEngine.PageStrategies;

import org.example.study.StrategyEngine.interfaces.PageGenerationStrategy;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

import static org.example.study.testData.TestData.getValidListForType;

//TODO: move invalid logic in here from GenericDtoInvalidStrategy
public class RandomStrategy implements PageGenerationStrategy {

    @Override
    public <T> List<T> generate(Class<T> clazz, int count) {
        return getValidListForType(clazz,10);
    }

    @Override
    public Object generateInvalidObj(Class<?> clazz, Field field, Class<? extends Annotation> annotationToBreak) throws NoSuchFieldException, IllegalAccessException {
        return null;
    }

    @Override
    public List<?> generateInvalidObjList(Class<?> clazz, int count) throws NoSuchFieldException, IllegalAccessException {
        return List.of();
    }
}
