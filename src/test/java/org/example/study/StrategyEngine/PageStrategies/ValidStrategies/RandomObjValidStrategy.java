package org.example.study.StrategyEngine.PageStrategies.ValidStrategies;

import org.example.study.StrategyEngine.interfaces.ValidDTOGenerationStrategy;

import java.util.List;


//TODO: complete it
public class RandomObjValidStrategy implements ValidDTOGenerationStrategy {

    @Override
    public <T> List<T> generateValidList(Class<T> clazz, int count) {
        return List.of();
    }

    @Override
    public Object generateValidObject(Class<?> clazz) {
        return null;
    }
}


