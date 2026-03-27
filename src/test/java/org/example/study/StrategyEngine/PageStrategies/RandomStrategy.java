package org.example.study.StrategyEngine.PageStrategies;

import org.example.study.StrategyEngine.interfaces.PageGenerationStrategy;

import java.util.List;

import static org.example.study.testData.TestData.getValidListForType;

//TODO: think to make it fully generic
public class RandomStrategy implements PageGenerationStrategy {

    @Override
    public List<?> generate(Class<?> clazz, int count) {
        return getValidListForType(clazz,10);
    }
}
