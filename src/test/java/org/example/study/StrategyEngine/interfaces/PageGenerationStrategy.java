package org.example.study.StrategyEngine.interfaces;

import java.util.List;

public interface PageGenerationStrategy {
    <T> List<T> generate(Class<T> clazz, int count);
}
