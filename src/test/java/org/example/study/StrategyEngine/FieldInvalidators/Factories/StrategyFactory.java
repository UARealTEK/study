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


    //TODO: SPLIT IT into two DISPATCHERS !
public class StrategyFactory {

    private Map<PageStrategyType, InvalidDTOGenerationStrategy> invalidStrategies;
    private Map<PageStrategyType, ValidDTOGenerationStrategy> validStrategies;

    public StrategyFactory(FieldInvalidationService service) {
        this.invalidStrategies = Map.of(
                PageStrategyType.RANDOM, new RandomObjInvalidStrategy(service),
                PageStrategyType.SAME, new SameObjInvalidStrategy(service),
                PageStrategyType.EMPTY, new EmptyObjInvalidStrategy());
    }

    public StrategyFactory() {
        this.validStrategies = Map.of(
                PageStrategyType.RANDOM, new RandomObjValidStrategy(),
                PageStrategyType.SAME, new SameObjValidStrategy(),
                PageStrategyType.EMPTY, new EmptyObjValidStrategy()
        );
    }

    public InvalidDTOGenerationStrategy getInvalidDTOGenerationStrategy(PageStrategyType type) {
        return invalidStrategies.get(type);
    }

    public ValidDTOGenerationStrategy getValidDTOGenerationStrategy(PageStrategyType type) {
        return validStrategies.get(type);
    }
}
