package org.example.study.StrategyEngine.PageStrategies.ValidStrategies;

import org.example.study.StrategyEngine.interfaces.ValidDTOGenerationStrategy;

import java.util.List;


public class EmptyObjValidStrategy implements ValidDTOGenerationStrategy {

    @Override
    public <T> List<T> generateValidList(Class<T> clazz, int count) {
        return List.of();
    }

    //TODO: Im not sure that throwing an exception is a correct approach
    @Override
    public <T> T generateValidObject(Class<T> clazz) {
        throw new UnsupportedOperationException("EmptyStrategy does not support generating objects.");
    }
}
