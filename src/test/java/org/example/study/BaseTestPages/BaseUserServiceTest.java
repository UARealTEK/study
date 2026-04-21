package org.example.study.BaseTestPages;

import org.example.study.repository.UserRepository;
import org.example.study.service.UserService;
import org.example.study.util.Converters.UserMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;

public abstract class BaseUserServiceTest extends BaseTest {

    @Mock
    protected UserRepository repository;
    protected UserService service;

    //TODO: move this one to BaseServiceTest
    protected UserMapper userMapper = Mappers.getMapper(UserMapper.class);


    @BeforeEach
    protected void init() {
        service = new UserService(repository, userMapper);
    }

    @AfterEach
    protected void cleanUp() {
        service = null;
    }

}
