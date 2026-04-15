package org.example.study.StrategyEngine.FieldInvalidators.Factories;

import org.example.study.StrategyEngine.FieldInvalidators.Services.FieldInvalidationService;
import org.example.study.StrategyEngine.PageStrategies.EmptyStrategy;
import org.example.study.StrategyEngine.PageStrategies.RandomStrategy;
import org.example.study.StrategyEngine.PageStrategies.SameObjStrategy;
import org.example.study.StrategyEngine.interfaces.InvalidDTOGenerationStrategy;
import org.example.study.enums.PageStrategyType;

import java.util.Map;

/**
 * Object generation dispatcher which produces currently available generation strategies
 * NOT really a factory though. Just a centralized tool to access a map that contains currently available strategies
 */

//TODO: look into it. Why it is tied up to INVALID data generation. Each strategy could be both
// if not - we might add more flags / create more strategies so factory can correctly produce desired result
public class InvalidStrategyFactory {

    Map<PageStrategyType, InvalidDTOGenerationStrategy> strategies;

    public InvalidStrategyFactory(FieldInvalidationService service) {
        this.strategies = Map.of(
                PageStrategyType.RANDOM, new RandomStrategy(service),
                PageStrategyType.SAME, new SameObjStrategy(service),
                PageStrategyType.EMPTY, new EmptyStrategy()
        );
    }

    public InvalidDTOGenerationStrategy getInvalidDTOGenerationStrategy(PageStrategyType type) {
        return strategies.get(type);
    }
}
