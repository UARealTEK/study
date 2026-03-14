package org.example.study.StrategyEngine.DTOStrategies;

import org.example.study.DTOs.BaseDao;
import org.example.study.DTOs.Entities.UserEntity;
import org.example.study.DTOs.UserDto;
import org.example.study.StrategyEngine.interfaces.InvalidDTOGenerationStrategy;

import static org.example.study.testData.TestData.getSingleValidForType;


public class NameStrategy<T extends BaseDao> implements InvalidDTOGenerationStrategy<T> {

    @SuppressWarnings("unchecked")
    @Override
    public T generate(Class<T> clazz) {
        BaseDao dao = getSingleValidForType(clazz);

        if (dao instanceof UserDto userDto) {
            userDto.setFullName("");
            return (T) userDto;
        } else if (dao instanceof UserEntity userEntity) {
            userEntity.setFullName("");
            return (T) userEntity;
        } else {
            throw new IllegalArgumentException("Unsupported class type: " + clazz.getName());
        }
    }
}
