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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.example.study.testData.TestData.getValidUsers;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class StudyApplicationTests {

    ObjectMapper mapper;
    List<UserDto> users;
    String usersEndpoint;
    String json;

    @BeforeEach
    void initValidUsers() {
        mapper = new ObjectMapper();
        users = getValidUsers();
        usersEndpoint = "/users";
        json = mapper.writeValueAsString(users);
    }

    @Autowired
    MockMvc mvc;

    @MockitoBean
    public UserService service;

    @Test
    void userFound() throws Exception {
        //given
        when(service.getAllUsers()).thenReturn(users);

        //when
        mvc.perform(get(usersEndpoint))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(json));
        //then
        verify(service, times(1)).getAllUsers();
    }

}
