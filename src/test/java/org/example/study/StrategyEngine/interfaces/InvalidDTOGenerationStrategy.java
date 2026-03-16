package org.example.study.StrategyEngine.interfaces;


import org.example.study.DTOs.BaseUser;

public interface InvalidDTOGenerationStrategy {
    <T extends BaseUser> T generate(Class<T> clazz);

    //TODO: finish this method
//    default <T extends BaseUser> T invalidateField(T obj, Field field) {
//
//    }
}
