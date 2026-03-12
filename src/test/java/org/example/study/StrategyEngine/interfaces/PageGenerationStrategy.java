package org.example.study.StrategyEngine.interfaces;

import java.util.List;

public interface PageGenerationStrategy {
    List<?> generate(Class<?> clazz, int count);
}
