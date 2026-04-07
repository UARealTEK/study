package org.example.study.StrategyEngine.PageStrategies;

import org.example.study.StrategyEngine.interfaces.PageGenerationStrategy;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

//TODO: move invalid logic in here from GenericDtoInvalidStrategy
public class EmptyStrategy implements PageGenerationStrategy {
    @Override
    public <T> List<T> generate(Class<T> clazz, int count) {
        return List.of();
    }

    @Override
    public Object generateInvalidObj(Class<?> clazz, Field field, Class<? extends Annotation> annotationToBreak) {
        return null;
    }

    @Override
    public List<?> generateInvalidObjList(Class<?> clazz, int count) {
        return List.of();
    }
}
