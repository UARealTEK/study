package org.example.study.StrategyEngine.interfaces;
import org.example.study.StrategyEngine.FieldInvalidators.MinInvalidator;

import java.util.List;
//TODO: This invalidates fields only for BaseUser.class DTOs. how do I make it fully compatible?
// refactoring this into FieldInvalidator engine

public interface InvalidDTOGenerationStrategy {
    List<FieldInvalidator> fieldInvalidators = List.of(
            new MinInvalidator()
    );

    <T> T generate(Class<T> clazz) throws NoSuchFieldException;
}
