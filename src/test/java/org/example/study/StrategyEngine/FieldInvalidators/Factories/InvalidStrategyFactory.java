package org.example.study.StrategyEngine.FieldInvalidators.Factories;

import org.example.study.StrategyEngine.FieldInvalidators.Services.FieldInvalidationService;
import org.example.study.StrategyEngine.PageStrategies.InvalidStrategies.EmptyObjInvalidStrategy;
import org.example.study.StrategyEngine.PageStrategies.InvalidStrategies.RandomObjInvalidStrategy;
import org.example.study.StrategyEngine.PageStrategies.InvalidStrategies.SameObjInvalidStrategy;
import org.example.study.StrategyEngine.PageStrategies.ValidStrategies.EmptyObjValidStrategy;
import org.example.study.StrategyEngine.PageStrategies.ValidStrategies.RandomObjValidStrategy;
import org.example.study.StrategyEngine.PageStrategies.ValidStrategies.SameObjValidStrategy;
import org.example.study.StrategyEngine.interfaces.InvalidDTOGenerationStrategy;
import org.example.study.StrategyEngine.interfaces.ValidDTOGenerationStrategy;
import org.example.study.enums.PageStrategyType;

import java.util.Map;

/**
 * Object generation dispatcher which produces currently available generation strategies
 * NOT really a factory though. Just a centralized tool to access a map that contains currently available strategies
 */

public class InvalidStrategyFactory {

    private Map<PageStrategyType, InvalidDTOGenerationStrategy> invalidStrategies;

    public InvalidStrategyFactory(FieldInvalidationService service) {
        this.invalidStrategies = Map.of(
                PageStrategyType.RANDOM, new RandomObjInvalidStrategy(service),
                PageStrategyType.SAME, new SameObjInvalidStrategy(service),
                PageStrategyType.EMPTY, new EmptyObjInvalidStrategy());
    }

    public InvalidDTOGenerationStrategy getInvalidDTOGenerationStrategy(PageStrategyType type) {
        return invalidStrategies.get(type);
    }
}
