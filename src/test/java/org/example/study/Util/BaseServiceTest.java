package org.example.study.Util;

import org.example.study.DTOs.UserDto;
import org.example.study.Entities.UserEntity;
import org.example.study.repository.UserRepository;
import org.example.study.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;

import static org.example.study.testData.TestData.*;
//TODO: clean up redundant variables because of the random resolvers
public abstract class BaseServiceTest extends BaseTest {

    @Mock
    protected UserRepository repository;
    @InjectMocks
    protected UserService service;
    protected Page<UserEntity> users;
    protected UserEntity user;
    protected UserDto userCopy;
    protected UserEntity invalidUser;

    @BeforeEach
    protected void init() {
        users = getValidUserEntityPage();
        user = getSingleValidEntity();
        invalidUser = getSingleEntityWithEmptyName();
        userCopy = userMapper.toUserDto(user);
        service = new UserService(repository, userMapper);
    }

    @AfterEach
    protected void cleanUp() {
        users = null;
        user = null;
        invalidUser = null;
        service = null;
    }

}
