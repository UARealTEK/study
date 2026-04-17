package org.example.study.StrategyEngine.PageStrategies.ValidStrategies;

import org.example.study.StrategyEngine.interfaces.ValidDTOGenerationStrategy;

import java.util.List;


public class EmptyObjValidStrategy implements ValidDTOGenerationStrategy {

    @Override
    public List<?> generateValidList(Class<?> clazz, int count) {
        return List.of();
    }

    //TODO: Im not sure that throwing an exception is a correct approach
    @Override
    public Object generateValidObject(Class<?> clazz) {
        throw new UnsupportedOperationException("EmptyStrategy does not support generating objects.");
    }
}
