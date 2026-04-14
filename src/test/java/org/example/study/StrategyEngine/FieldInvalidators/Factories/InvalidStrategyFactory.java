package org.example.study.StrategyEngine.FieldInvalidators.Factories;

import org.example.study.StrategyEngine.PageStrategies.EmptyStrategy;
import org.example.study.StrategyEngine.PageStrategies.RandomStrategy;
import org.example.study.StrategyEngine.PageStrategies.SameObjStrategy;
import org.example.study.StrategyEngine.interfaces.InvalidDTOGenerationStrategy;
import org.example.study.enums.PageStrategyType;

import java.util.Map;

/**
 * Object generation dispatcher which produces currently available generation strategies
 */
public class InvalidStrategyFactory {

    Map<PageStrategyType, InvalidDTOGenerationStrategy> pageStrategyMap;

    InvalidStrategyFactory() {
        this.pageStrategyMap = Map.of(
                PageStrategyType.RANDOM, new RandomStrategy(),
                PageStrategyType.SAME, new SameObjStrategy(),
                PageStrategyType.EMPTY, new EmptyStrategy()
        );
    }

    public InvalidDTOGenerationStrategy getInvalidDTOGenerationStrategy(PageStrategyType pageStrategyType) {
        return pageStrategyMap.get(pageStrategyType);
    }
}
