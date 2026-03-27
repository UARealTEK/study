package org.example.study.StrategyEngine.PageStrategies;

import org.example.study.StrategyEngine.interfaces.PageGenerationStrategy;

import java.util.List;
import java.util.stream.Stream;

import static org.example.study.testData.TestData.getValidListForType;

//TODO: think to make it fully generic
public class SameObjStrategy implements PageGenerationStrategy {
    @Override
    public List<?> generate(Class<?> clazz, int count) {
        List<?> list = getValidListForType(clazz,1);
        return Stream.generate(list::getFirst).limit(count).toList();
    }
}
