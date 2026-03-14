package org.example.study.StrategyEngine.interfaces;


import org.example.study.DTOs.BaseDao;

//TODO: figure ouw how generic bounds work. Check syntax in concrete Implementations
public interface InvalidDTOGenerationStrategy<T extends BaseDao> {
    T generate(Class<T> clazz);
}
