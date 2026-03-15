package org.example.study.StrategyEngine.interfaces;


import org.example.study.DTOs.BaseUser;

public interface InvalidDTOGenerationStrategy<T extends BaseUser> {
    T generate(Class<T> clazz);
}
