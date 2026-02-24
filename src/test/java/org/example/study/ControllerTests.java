package org.example.study;

import org.example.study.DTOs.PageResponseDTO;
import org.example.study.DTOs.UserDto;
import org.example.study.Util.BaseControllerTest;
import org.example.study.controller.UserController;
import org.example.study.service.UserService;
import org.example.study.util.Exceptions.CustomExceptions.UserNotFoundException;
import org.example.study.util.Exceptions.ExceptionHandler.ExceptionDto;
import org.example.study.util.Exceptions.ExceptionHandler.FieldErrorDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.servlet.NoHandlerFoundException;
import tools.jackson.core.type.TypeReference;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.study.DTOs.UserDto.copyOf;
import static org.example.study.testData.TestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class ControllerTests extends BaseControllerTest {

    @Autowired
    MockMvc mvc;

    @MockitoBean
    public UserService service;

    @Test
    void testFindAllUsers() throws Exception {
        //given
        when(service.getAllUsers(any(Pageable.class))).thenReturn(users);

        //when
        mvc.perform(get(usersEndpoint))
                .andExpect(status().isOk())
                .andExpect(content().json(usersJson));
        //then
        verify(service, times(1)).getAllUsers(any(Pageable.class));
    }

    //TODO: rework using PageResponseDTO
    @ParameterizedTest
    @MethodSource("org.example.study.testData.TestData#getValidUserDtoPageStream")
    void checkPagination(Page<UserDto> dto) throws Exception {
        //when
        when(service.getAllUsers(any(Pageable.class))).thenReturn(dto);

        MvcResult result = mvc.perform(get(usersEndpoint)
                .param("page", String.valueOf(dto.getNumber()))
                .param("size", String.valueOf(dto.getSize()))
        ).andReturn();

        PageResponseDTO<UserDto> resultPage = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});

        verify(service).getAllUsers(argThat(
                i -> i.getPageNumber() == dto.getNumber() &&
                        i.getPageSize() == dto.getSize()));

        verify(service, times(1)).getAllUsers(any(Pageable.class));
        verifyNoMoreInteractions(service);

        assertAll(
                () -> assertEquals(dto.getNumber(), resultPage.number()), // current page Number
                () -> assertEquals(dto.getSize(), resultPage.size()), // declared size of the page (not the amount of displayed content elements)
                () -> assertEquals(resultPage.totalElements(), dto.getTotalElements()), // total amount of elements in DB
                () -> assertEquals(resultPage.totalPages(), dto.getTotalPages()), // calculated value (total elements / size)

                () -> assertEquals(resultPage.content().get(0).getAge(), dto.getContent().get(0).getAge()),
                () -> assertEquals(resultPage.content().get(0).getFullName(), dto.getContent().get(0).getFullName()),
                () -> assertEquals(resultPage.content().get(0).getGender(), dto.getContent().get(0).getGender()),

                () -> assertEquals(resultPage.content().get(1).getAge(), dto.getContent().get(1).getAge()),
                () -> assertEquals(resultPage.content().get(1).getFullName(), dto.getContent().get(1).getFullName()),
                () -> assertEquals(resultPage.content().get(1).getGender(), dto.getContent().get(1).getGender()),

                () -> assertEquals(resultPage.content().get(2).getAge(), dto.getContent().get(2).getAge()),
                () -> assertEquals(resultPage.content().get(2).getFullName(), dto.getContent().get(2).getFullName()),
                () -> assertEquals(resultPage.content().get(2).getGender(), dto.getContent().get(2).getGender())
        );
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
        FieldErrorDto fullNameError = new FieldErrorDto("fullName", "name should not be blank");
        FieldErrorDto nameIsMandatory = new FieldErrorDto("fullName", "name is mandatory and its length should be in range of 1 - 100");

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST.toString(), exceptionDto.statusCode().toString()),
                () -> assertEquals("Validation error", exceptionDto.type()),
                () -> assertEquals("Incorrect method arguments provided", exceptionDto.message()),
                () -> assertThat(fullNameError).isIn(exceptionDto.exceptionMessage()),
                () -> assertThat(nameIsMandatory).isIn(exceptionDto.exceptionMessage())
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
        FieldErrorDto fullNameError = new FieldErrorDto("fullName", "name should not be blank");
        FieldErrorDto nameIsMandatory = new FieldErrorDto("fullName", "name is mandatory and its length should be in range of 1 - 100");

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST.toString(), response.statusCode().toString()),
                () -> assertEquals("Validation error", response.type()),
                () -> assertEquals("Incorrect method arguments provided", response.message()),
                () -> assertThat(fullNameError).isIn(response.exceptionMessage()),
                () -> assertThat(nameIsMandatory).isIn(response.exceptionMessage())
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

    //Checking this method by trying to perform a GET request without /users endpoint
    @Test
    void checkNoHandlerFound() throws Exception {
        //when
        mvc.perform(get("/"))
                .andExpect(status().isNotFound())
                .andExpect(i -> assertInstanceOf(NoHandlerFoundException.class, i.getResolvedException()));
        //then
        verifyNoInteractions(service);
    }

}
