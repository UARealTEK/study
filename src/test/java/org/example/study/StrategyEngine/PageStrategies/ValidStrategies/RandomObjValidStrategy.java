package org.example.study.StrategyEngine.PageStrategies.ValidStrategies;

import org.example.study.StrategyEngine.interfaces.ValidDTOGenerationStrategy;

import java.util.List;

import static org.example.study.testData.TestData.getSingleValidForType;
import static org.example.study.testData.TestData.getValidListForType;


public class RandomObjValidStrategy implements ValidDTOGenerationStrategy {

    @Override
    public <T> List<T> generateValidList(Class<T> clazz, int count) {
        return getValidListForType(clazz,count);
    }

    @Override
    public <T> T generateValidObject(Class<T> clazz) {
        return getSingleValidForType(clazz);
    }
}


