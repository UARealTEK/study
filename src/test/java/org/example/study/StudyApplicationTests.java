package org.example.study;

import org.example.study.DTOs.UserDto;
import org.example.study.controller.UserController;
import org.example.study.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.example.study.testData.TestData.getSingleValidUser;
import static org.example.study.testData.TestData.getValidUsers;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class StudyApplicationTests {

    ObjectMapper mapper;
    List<UserDto> users;
    UserDto user;
    String usersEndpoint;
    String usersJson;
    String singleUserJson;

    @BeforeEach
    void initValidUsers() {
        mapper = new ObjectMapper();
        users = getValidUsers();
        user = getSingleValidUser();
        usersEndpoint = "/users";
        usersJson = mapper.writeValueAsString(users);
        singleUserJson = mapper.writeValueAsString(user);
    }

    @Autowired
    MockMvc mvc;

    @MockitoBean
    public UserService service;

    @Test
    void usersFound() throws Exception {
        //given
        when(service.getAllUsers()).thenReturn(users);

        //when
        mvc.perform(get(usersEndpoint))
                .andExpect(status().isOk())
                .andExpect(content().json(usersJson));
        //then
        verify(service, times(1)).getAllUsers();
    }

    @Test
    void findSingleValidUser() throws Exception {
        //given
        when(service.getUserByID(any(Long.class))).thenReturn(getSingleValidUser());
        //when
        mvc.perform(get(usersEndpoint + "/" + any(Long.class)))
                .andExpect(status().isOk())
                .andExpect(content().json(singleUserJson));
        //then
        verify(service, times(1)).getUserByID(any(Long.class));
    }

}
