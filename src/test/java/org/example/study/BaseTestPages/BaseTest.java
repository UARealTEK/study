package org.example.study.BaseTestPages;

import org.example.study.util.Converters.BookMapper;
import org.example.study.util.Converters.BorrowRecordMapper;
import org.example.study.util.Converters.UserMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;

@SuppressWarnings("unused")
public abstract class BaseTest {

    protected final UserMapper userMapper = Mappers.getMapper(UserMapper.class);
    protected final BookMapper bookMapper = Mappers.getMapper(BookMapper.class);
    protected final BorrowRecordMapper borrowRecordMapper = Mappers.getMapper(BorrowRecordMapper.class);

    @Autowired
    protected ObjectMapper mapper;
    protected void init(){}
    protected void cleanUp(){}

    @SuppressWarnings("unchecked")
    protected <T> Specification<T> anySpec() {
        return any(Specification.class);
    }
}
