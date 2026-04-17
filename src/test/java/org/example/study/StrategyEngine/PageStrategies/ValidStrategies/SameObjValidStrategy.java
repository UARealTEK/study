package org.example.study.StrategyEngine.PageStrategies.ValidStrategies;

import org.example.study.StrategyEngine.interfaces.ValidDTOGenerationStrategy;

import java.util.List;
import java.util.stream.Stream;

import static org.example.study.testData.TestData.getSingleValidForType;
import static org.example.study.testData.TestData.getValidListForType;

public class SameObjValidStrategy implements ValidDTOGenerationStrategy {

    @Override
    public List<?> generateValidList(Class<?> clazz, int count) {
        List<?> list = getValidListForType(clazz,count);
        return Stream.generate(list::getFirst).limit(count).toList();
    }

    @Override
    public Object generateValidObject(Class<?> clazz) {
        return getSingleValidForType(clazz);
    }
}


