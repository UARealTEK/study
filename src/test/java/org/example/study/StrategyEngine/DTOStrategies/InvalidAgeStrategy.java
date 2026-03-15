package org.example.study.StrategyEngine.DTOStrategies;

import org.example.study.DTOs.BaseUser;
import org.example.study.StrategyEngine.interfaces.InvalidDTOGenerationStrategy;

import static org.example.study.testData.TestData.getSingleValidForType;


public class InvalidAgeStrategy<T extends BaseUser> implements InvalidDTOGenerationStrategy<T> {

    @Override
    public T generate(Class<T> clazz) {
        T dao = getSingleValidForType(clazz);
        dao.setAge(0);
        return dao;
    }
}
