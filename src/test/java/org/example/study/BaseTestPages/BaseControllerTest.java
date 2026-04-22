package org.example.study.BaseTestPages;

import org.example.study.util.Converters.BookMapper;
import org.example.study.util.Converters.BorrowRecordMapper;
import org.example.study.util.Converters.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;

//TODO: SPLIT into BaseSpringTest and BaseServiceTest (so I have separate Mappers. Autowired ones for WebMvc Tests and simple DI for Service Tests)
@SuppressWarnings("unused")
public abstract class BaseControllerTest {

    @Autowired
    protected UserMapper userMapper;
    @Autowired
    protected BookMapper bookMapper;
    @Autowired
    protected BorrowRecordMapper borrowRecordMapper;

    @Autowired
    protected ObjectMapper mapper;
    protected void init(){}
    protected void cleanUp(){}

    @SuppressWarnings("unchecked")
    protected <T> Specification<T> anySpec() {
        return any(Specification.class);
    }
}
