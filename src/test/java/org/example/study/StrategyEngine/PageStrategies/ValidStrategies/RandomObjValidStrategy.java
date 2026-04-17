package org.example.study.StrategyEngine.PageStrategies.ValidStrategies;

import org.example.study.StrategyEngine.interfaces.ValidDTOGenerationStrategy;

import java.util.List;

import static org.example.study.testData.TestData.getSingleValidForType;
import static org.example.study.testData.TestData.getValidListForType;


public class RandomObjValidStrategy implements ValidDTOGenerationStrategy {

    @Override
    public List<?> generateValidList(Class<?> clazz, int count) {
        return getValidListForType(clazz,count);
    }

    @Override
    public Object generateValidObject(Class<?> clazz) {
        return getSingleValidForType(clazz);
    }
}


