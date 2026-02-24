package org.example.study.Util;

import org.example.study.DTOs.PageResponseDTO;
import org.example.study.DTOs.UserDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import tools.jackson.databind.ObjectMapper;

import static org.example.study.testData.TestData.*;

//TODO: Refactor using Pageable
public class BaseControllerTest extends BaseTest {

    @Autowired
    protected ObjectMapper mapper;
    protected PageResponseDTO<UserDto> users;
    protected UserDto user;
    protected UserDto invalidUser;
    protected final String usersEndpoint = "/users";
    protected String usersJson;
    protected String singleUserJson;
    protected String singleInvalidUserJson;

    @BeforeEach
    protected void init() {
        mapper = new ObjectMapper();
        users = getValidUserDtoPage();
        user = getSingleValidUser();
        invalidUser = getSingleUserWithEmptyName();
        usersJson = mapper.writeValueAsString(users);
        singleUserJson = mapper.writeValueAsString(user);
        singleInvalidUserJson = mapper.writeValueAsString(invalidUser);
    }

    @AfterEach
    protected void cleanUp() {
        mapper = null;
        users = null;
        user = null;
        invalidUser = null;
        usersJson = null;
        singleUserJson = null;
        singleInvalidUserJson = null;
    }
}
