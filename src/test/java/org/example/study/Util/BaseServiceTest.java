package org.example.study.Util;

import org.example.study.DTOs.UserDto;
import org.example.study.DTOs.UserEntity;
import org.example.study.util.Converters.Converter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.util.List;

import static org.example.study.testData.TestData.*;

public class BaseServiceTest extends BaseTest {

    protected List<UserEntity> users;
    protected UserEntity user;
    protected UserDto userCopy;
    protected UserEntity invalidUser;

    @BeforeEach
    protected void init() {
        users = getValidEntities();
        user = getSingleValidEntity();
        invalidUser = getSingleEntityWithEmptyName();
        userCopy = Converter.toUserDto(user);
    }

    @AfterEach
    protected void cleanUp() {
        users = null;
        user = null;
        invalidUser = null;
    }

}
