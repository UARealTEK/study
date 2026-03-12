package org.example.study.StrategyEngine.Strategies;

import org.example.study.StrategyEngine.interfaces.PageGenerationStrategy;

import java.util.List;

public class EmptyStrategy implements PageGenerationStrategy {
    @Override
    public List<?> generate(Class<?> clazz, int count) {
        return List.of();
    }
}
