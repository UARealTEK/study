package org.example.study.Util;

import org.example.study.util.Converters.UserMapper;
import org.mapstruct.factory.Mappers;

@SuppressWarnings("unused")
public abstract class BaseTest {

    protected final UserMapper mapper = Mappers.getMapper(UserMapper.class);
    protected void init(){}
    protected void cleanUp(){}
}
