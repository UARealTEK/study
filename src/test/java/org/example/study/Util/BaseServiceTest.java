package org.example.study.Util;

import org.example.study.DTOs.UserEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.util.List;

import static org.example.study.testData.TestData.*;

public class BaseServiceTest extends BaseTest {

    protected List<UserEntity> users;
    protected UserEntity user;
    protected UserEntity invalidUser;

    @BeforeEach
    protected void init() {
        users = getValidEntities();
        user = getSingleValidEntity();
        invalidUser = getSingleEntityWithEmptyName();
    }

    @AfterEach
    protected void cleanUp() {
        users = null;
        user = null;
        invalidUser = null;
    }

}
