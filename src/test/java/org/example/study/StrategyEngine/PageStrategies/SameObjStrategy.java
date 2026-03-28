package org.example.study.StrategyEngine.PageStrategies;

import org.example.study.StrategyEngine.interfaces.PageGenerationStrategy;

import java.util.List;
import java.util.stream.Stream;

import static org.example.study.testData.TestData.getValidListForType;

public class SameObjStrategy implements PageGenerationStrategy {
    @Override
    public <T> List<T> generate(Class<T> clazz, int count) {
        List<T> list = getValidListForType(clazz,1);
        return Stream.generate(list::getFirst).limit(count).toList();
    }
}
