package org.example.study.StrategyEngine.PageStrategies;

import org.example.study.StrategyEngine.interfaces.PageGenerationStrategy;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Stream;

import static org.example.study.testData.TestData.getValidListForType;

//TODO: move invalid logic in here from GenericDtoInvalidStrategy
public class SameObjStrategy implements PageGenerationStrategy {
    @Override
    public <T> List<T> generate(Class<T> clazz, int count) {
        List<T> list = getValidListForType(clazz,1);
        return Stream.generate(list::getFirst).limit(count).toList();
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
