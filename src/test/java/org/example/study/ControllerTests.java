package org.example.study;

import org.example.study.DTOs.UserDto;
import org.example.study.controller.UserController;
import org.example.study.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.example.study.testData.TestData.getSingleValidUser;
import static org.example.study.testData.TestData.getValidUsers;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class ControllerTests {

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
    void testFindAllUsers() throws Exception {
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
    void testFindSingleValidUser() throws Exception {
        //given
        when(service.getUserByID(any(Long.class))).thenReturn(getSingleValidUser());
        //when
        mvc.perform(get(usersEndpoint + "/" + any(Long.class)))
                .andExpect(status().isOk())
                .andExpect(content().json(singleUserJson));
        //then
        verify(service, times(1)).getUserByID(any(Long.class));
    }

    @Test
    void testSaveValidUser() throws Exception {
        //given
        when(service.saveUser(any(UserDto.class))).thenReturn(user);

        //when
        mvc.perform(post(usersEndpoint)
                .contentType(MediaType.APPLICATION_JSON).content(singleUserJson))
                .andExpect(status().isCreated())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(singleUserJson));
        //then

        //Tried to use captor here to see how it works. basically it tracks the argument that crossed from controller to service
        //Then I can inspect what DTO came to the service. Check if it matches the DTO that I'm intended to use further
        ArgumentCaptor<UserDto> captor = ArgumentCaptor.forClass(UserDto.class);
        verify(service).saveUser(captor.capture());
        UserDto dto = captor.getValue();

        //checks to verify that DTO which was serialized from JSON and passed to the service layer contains correct data
        assertAll(
                () -> assertEquals(dto.getAge(), user.getAge()),
                () -> assertEquals(dto.getGender(), user.getGender()),
                () -> assertEquals(dto.getFullName(), user.getFullName())
        );

        verifyNoMoreInteractions(service);
    }

}
