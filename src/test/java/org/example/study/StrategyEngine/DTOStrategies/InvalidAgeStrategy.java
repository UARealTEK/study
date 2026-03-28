package org.example.study.StrategyEngine.DTOStrategies;

import org.example.study.StrategyEngine.interfaces.InvalidDTOGenerationStrategy;

import static org.example.study.testData.TestData.getSingleValidForType;


public class InvalidAgeStrategy implements InvalidDTOGenerationStrategy {

    @Override
    public<T> T generate(Class<T> clazz) throws NoSuchFieldException {
        T dao = getSingleValidForType(clazz);
        return invalidateField(dao, dao.getClass().getField("age"));
    }
}
