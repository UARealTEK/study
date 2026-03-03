package org.example.study.Util;

import org.example.study.util.Converters.UserMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.data.jpa.domain.Specification;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;

@SuppressWarnings("unused")
public abstract class BaseTest {

    protected final UserMapper userMapper = Mappers.getMapper(UserMapper.class);
    protected static ObjectMapper mapper = new ObjectMapper();
    protected void init(){}
    protected void cleanUp(){}

    @SuppressWarnings("unchecked")
    protected <T> Specification<T> anySpec() {
        return any(Specification.class);
    }
}
