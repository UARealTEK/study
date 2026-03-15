package org.example.study.StrategyEngine.DTOStrategies;

import org.example.study.DTOs.BaseUser;
import org.example.study.StrategyEngine.interfaces.InvalidDTOGenerationStrategy;

import static org.example.study.testData.TestData.getSingleValidForType;


public class InvalidGenderStrategy<T extends BaseUser> implements InvalidDTOGenerationStrategy<T> {

    @SuppressWarnings("ConstantConditions")
    @Override
    public T generate(Class<T> clazz) {
        T dao = getSingleValidForType(clazz);
        dao.setGender(null);
        return dao;
    }
}
