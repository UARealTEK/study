package org.example.study.StrategyEngine.PageStrategies.ValidStrategies;

import org.example.study.StrategyEngine.interfaces.ValidDTOGenerationStrategy;

import java.util.List;
import java.util.stream.Stream;

import static org.example.study.testData.TestData.getSingleValidForType;
import static org.example.study.testData.TestData.getValidListForType;

public class SameObjValidStrategy implements ValidDTOGenerationStrategy {

    @Override
    public <T> List<T> generateValidList(Class<T> clazz, int count) {
        List<T> list = getValidListForType(clazz,count);
        return Stream.generate(list::getFirst).limit(count).toList();
    }

    @Override
    public <T> T generateValidObject(Class<T> clazz) {
        return getSingleValidForType(clazz);
    }
}