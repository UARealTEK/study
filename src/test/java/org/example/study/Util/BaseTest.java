package org.example.study.Util;

import org.example.study.util.Converters.UserMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.data.jpa.domain.Specification;

import static org.mockito.ArgumentMatchers.any;

@SuppressWarnings("unused")
public abstract class BaseTest {

    protected final UserMapper mapper = Mappers.getMapper(UserMapper.class);
    protected void init(){}
    protected void cleanUp(){}

    @SuppressWarnings("unchecked")
    protected <T> Specification<T> anySpec() {
        return any(Specification.class);
    }
}
