package org.example.study.StrategyEngine.FieldInvalidators.Factories;

import org.example.study.StrategyEngine.PageStrategies.ValidStrategies.EmptyObjValidStrategy;
import org.example.study.StrategyEngine.PageStrategies.ValidStrategies.RandomObjValidStrategy;
import org.example.study.StrategyEngine.PageStrategies.ValidStrategies.SameObjValidStrategy;
import org.example.study.StrategyEngine.interfaces.ValidDTOGenerationStrategy;
import org.example.study.enums.PageStrategyType;

import java.util.Map;

/**
 * Object generation dispatcher which produces currently available generation strategies
 * NOT really a factory though. Just a centralized tool to access a map that contains currently available strategies
 */


    //TODO: SPLIT IT into two DISPATCHERS !
public class ValidStrategyFactory {

    private final Map<PageStrategyType, ValidDTOGenerationStrategy> validStrategies;

    public ValidStrategyFactory() {
        this.validStrategies = Map.of(
                PageStrategyType.RANDOM, new RandomObjValidStrategy(),
                PageStrategyType.SAME, new SameObjValidStrategy(),
                PageStrategyType.EMPTY, new EmptyObjValidStrategy()
        );
    }

    public ValidDTOGenerationStrategy getValidDTOGenerationStrategy(PageStrategyType type) {
        return validStrategies.get(type);
    }
}
