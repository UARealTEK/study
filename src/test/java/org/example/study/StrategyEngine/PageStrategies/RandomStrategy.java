package org.example.study.StrategyEngine.PageStrategies;

import org.example.study.StrategyEngine.interfaces.PageGenerationStrategy;

import java.util.List;

import static org.example.study.testData.TestData.getValidListForType;

public class RandomStrategy implements PageGenerationStrategy {

    @Override
    public <T> List<T> generate(Class<T> clazz, int count) {
        return getValidListForType(clazz,10);
    }
}
