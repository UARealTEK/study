package org.example.study;

import org.example.study.DTOs.UserDto;
import org.example.study.controller.UserController;
import org.example.study.service.UserService;
import org.example.study.util.Exceptions.CustomExceptions.UserNotFoundException;
import org.example.study.util.Exceptions.ExceptionHandler.ExceptionDto;
import org.example.study.util.Exceptions.ExceptionHandler.FieldErrorDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Objects;

import static org.example.study.DTOs.UserDto.copyOf;
import static org.example.study.testData.TestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class ControllerTests {

    ObjectMapper mapper;
    List<UserDto> users;
    UserDto user;
    UserDto invalidUser;
    String usersEndpoint;
    String usersJson;
    String singleUserJson;
    String singleInvalidUserJson;

    @BeforeEach
    void initValidUsers() {
        mapper = new ObjectMapper();
        users = getValidUsers();
        user = getSingleValidUser();
        invalidUser = getSingleUserWithEmptyName();
        usersEndpoint = "/users";
        usersJson = mapper.writeValueAsString(users);
        singleUserJson = mapper.writeValueAsString(user);
        singleInvalidUserJson = mapper.writeValueAsString(invalidUser);
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

    // Test that exception is thrown
    // in this case - MethodArgumentNotValid. Because I validate request body at Controller level
    // So validation does not reach the service at all
    // As for whether the correct exception was thrown - I assert on the MESSAGES that are returned to the user as the result of this call
    @Test
    void checkSaveInvalidUserValidation() throws Exception {
        //when
        MvcResult result = mvc.perform(post(usersEndpoint)
                        .contentType(MediaType.APPLICATION_JSON).content(singleInvalidUserJson))
                .andReturn();
        //then

        ExceptionDto exceptionDto = mapper.readValue(result.getResponse().getContentAsString(), ExceptionDto.class);

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST.toString(), exceptionDto.statusCode().toString()),
                () -> assertEquals("Validation error", exceptionDto.type()),
                () -> assertEquals("Incorrect method arguments provided", exceptionDto.message()),
                () -> assertEquals(List.of(
                        new FieldErrorDto("fullName", "name should not be blank")
                ), exceptionDto.exceptionMessage())
        );

        verifyNoInteractions(service);
    }

    @Test
    void checkValidUpdateUser() throws Exception {
        UserDto updatedUser = copyOf(user);
        updatedUser.setAge(user.getAge() + 10);

        when(service.updateUser(any(UserDto.class), eq(100L))).thenReturn(updatedUser);

        mvc.perform(put(usersEndpoint + "/" + 100L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updatedUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.age").value(updatedUser.getAge()));

        verify(service, times(1)).updateUser(any(UserDto.class), eq(100L));
    }

    @Test
    void checkUpdateUserUsingInvalidDto() throws Exception {
        UserDto updatedUser = copyOf(user);
        updatedUser.setFullName("");

        //using PUT on user with ID 1 but that's not ideal. User might not exist and then the test will die
        MvcResult result = mvc.perform(put(usersEndpoint + "/" + 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updatedUser)))
                .andExpect(i -> assertInstanceOf(MethodArgumentNotValidException.class, i.getResolvedException()))
                .andReturn();

        ExceptionDto response = mapper.readValue(result.getResponse().getContentAsString(), ExceptionDto.class);

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST.toString(), response.statusCode().toString()),
                () -> assertEquals("Validation error", response.type()),
                () -> assertEquals("Incorrect method arguments provided", response.message()),
                () -> assertEquals(List.of(
                        new FieldErrorDto("fullName", "name should not be blank")
                ), response.exceptionMessage())
        );

        verifyNoInteractions(service);
    }

    @Test
    void checkDeleteValidUser() throws Exception {
        //given
        doNothing().when(service).deleteUser(any(Long.class));
        //when

        mvc.perform(delete(usersEndpoint + "/" + 1L))
                .andExpect(status().isNoContent());
        //then

        verify(service, times(1)).deleteUser(eq(1L));
    }

    @Test
    void checkDeleteInvalidUser() throws Exception {
        //given
        doThrow(new UserNotFoundException()).when(service).deleteUser(any(Long.class));
        //when
        MvcResult result = mvc.perform(delete(usersEndpoint + "/" + 1L))
                .andExpect(i -> assertEquals(UserNotFoundException.class, Objects.requireNonNull(i.getResolvedException()).getClass()))
                .andReturn();

        //then
        ExceptionDto dto = mapper.readValue(result.getResponse().getContentAsString(), ExceptionDto.class);

        assertAll(
                () -> assertEquals(HttpStatus.NOT_FOUND.toString(), dto.statusCode().toString()),
                () -> assertEquals("Not found", dto.type()),
                () -> assertEquals("User was NOT found", dto.message()),
                () -> assertNull(dto.exceptionMessage())
        );

        verify(service, times(1)).deleteUser(any(Long.class));
    }

}
