package org.example.study.Util;

import org.example.study.repository.UserRepository;
import org.example.study.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public abstract class BaseServiceTest extends BaseTest {

    @Mock
    protected UserRepository repository;
    @InjectMocks
    protected UserService service;

    @BeforeEach
    protected void init() {
        service = new UserService(repository, userMapper);
    }

    @AfterEach
    protected void cleanUp() {
        service = null;
    }

}
