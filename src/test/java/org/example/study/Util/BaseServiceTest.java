package org.example.study.Util;

import org.example.study.DTOs.UserDto;
import org.example.study.Entities.UserEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.data.domain.Page;

import static org.example.study.testData.TestData.*;

public class BaseServiceTest extends BaseTest {

    protected Page<UserEntity> users;
    protected UserEntity user;
    protected UserDto userCopy;
    protected UserEntity invalidUser;

    @BeforeEach
    protected void init() {
        users = getValidUserEntityPage();
        user = getSingleValidEntity();
        invalidUser = getSingleEntityWithEmptyName();
        userCopy = mapper.toUserDto(user);
    }

    @AfterEach
    protected void cleanUp() {
        users = null;
        user = null;
        invalidUser = null;
    }

}
