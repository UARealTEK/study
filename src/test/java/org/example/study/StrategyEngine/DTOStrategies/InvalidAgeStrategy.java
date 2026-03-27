package org.example.study.StrategyEngine.DTOStrategies;

import org.example.study.DTOs.BaseUser;
import org.example.study.StrategyEngine.interfaces.InvalidDTOGenerationStrategy;

import static org.example.study.testData.TestData.getSingleValidForType;


//TODO: think to make it fully generic
public class InvalidAgeStrategy implements InvalidDTOGenerationStrategy {

    @Override
    public<T extends BaseUser> T generate(Class<T> clazz) throws NoSuchFieldException {
        T dao = getSingleValidForType(clazz);
        return invalidateField(dao, dao.getClass().getField("age"));
    }
}
