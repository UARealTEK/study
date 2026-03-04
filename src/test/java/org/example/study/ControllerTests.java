package org.example.study;

import org.example.study.DTOs.PageResponseDTO;
import org.example.study.DTOs.UserDto;
import org.example.study.Util.BaseControllerTest;
import org.example.study.Annotations.Smoke;
import org.example.study.enums.Endpoints;
import org.example.study.enums.Gender;
import org.example.study.util.Exceptions.CustomExceptions.UserNotFoundException;
import org.example.study.util.Exceptions.ExceptionHandler.ExceptionDto;
import org.example.study.util.Exceptions.ExceptionHandler.FieldErrorDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.servlet.NoHandlerFoundException;
import tools.jackson.core.type.TypeReference;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.study.DTOs.UserDto.copyOf;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Smoke
class ControllerTests extends BaseControllerTest {

    @Test
    void testFindAllUsers() throws Exception {
        //given
        when(service.getAllUsers(any(Pageable.class),
                isNull(),
                isNull(),
                isNull()))
                .thenReturn(users);

        //when
        steps.mvcGet()
                .andExpect(content().json(usersJson));
        //then
        verify(service, times(1)).getAllUsers(any(Pageable.class), isNull(), isNull(), isNull());
    }

    @ParameterizedTest
    @MethodSource("org.example.study.testData.TestData#getValidUserDtoPageStream")
    void checkPagination(PageResponseDTO<UserDto> dto) throws Exception {
        //when
        when(service.getAllUsers(any(Pageable.class), isNull(), isNull(), isNull())).thenReturn(dto);

        //then
        MvcResult result = steps.mvcGet(Map.of(
                "page", String.valueOf(dto.number()),
                "size", String.valueOf(dto.size()))
        ).andReturn();

        PageResponseDTO<UserDto> resultPage = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});

        verify(service).getAllUsers(argThat(
                pageable -> {
                    assertThat(pageable.getPageNumber()).isEqualTo(dto.number());
                    assertThat(pageable.getPageSize()).isEqualTo(dto.size());
                    return true;
                }
        ), isNull(),isNull(),isNull());

        verify(service, times(1)).getAllUsers(any(Pageable.class),isNull(),isNull(),isNull());
        verifyNoMoreInteractions(service);

        assertThat(dto)
                .usingRecursiveComparison()
                .isEqualTo(resultPage);
    }

    @ParameterizedTest
    @MethodSource("org.example.study.testData.TestData#getSingleValidUserArg")
    void testFindSingleValidUser(UserDto userDto) throws Exception {
        //given
        when(service.getUserByID(any(Long.class))).thenReturn(userDto);
        //when
        steps.mvcGet(1L)
                .andExpect(status().isOk())
                .andExpect(content().json(singleUserJson));
        //then
        verify(service, times(1)).getUserByID(any(Long.class));
    }

    @Test
    void checkFindInvalidUser() throws Exception {
        //given
        doThrow(new UserNotFoundException(99L)).when(service).getUserByID(anyLong());
        //when

        MvcResult result = steps.mvcGet(99L)
                .andExpect(status().isNotFound())
                .andReturn();
        //then
        ExceptionDto exceptionDto = mapper.readValue(result.getResponse().getContentAsString(), ExceptionDto.class);

        assertAll(
                () -> assertEquals(HttpStatus.NOT_FOUND.toString(), exceptionDto.statusCode().toString()),
                () -> assertEquals("Not found", exceptionDto.type()),
                () -> assertEquals("User was NOT found", exceptionDto.message()),
                () -> assertEquals(new UserNotFoundException(99L).getMessage(), exceptionDto.exceptionMessage().get(0).message())
        );

        verify(service, times(1)).getUserByID(99L);
    }

    @ParameterizedTest
    @MethodSource("org.example.study.testData.TestData#getValidUserStream")
    void testFindSingleValidUserUsingParams(List<UserDto> dto) throws Exception {
        //given
        Map<String,String> params = Map.of(
                "age", String.valueOf(dto.get(0).getAge()),
                "fullName", dto.get(0).getFullName(),
                "gender", dto.get(0).getGender().name().toLowerCase(),
                "page", String.valueOf(0),
                "size", String.valueOf(dto.size())
        );

        Page<UserDto> page = new PageImpl<>(dto, PageRequest.of(0,dto.size()), dto.size());

        //when
        when(service.getAllUsers(any(Pageable.class),anyInt(),anyString(),any(Gender.class))).thenReturn(userMapper.toPageResponse(page));

        //then
        MvcResult result = steps.mvcGet(params)
                .andExpect(status().isOk())
                .andReturn();

        PageResponseDTO<UserDto> resultPage = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
        ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);

        verify(service).getAllUsers(captor.capture(),
                eq(dto.get(0).getAge()),
                eq(dto.get(0).getFullName()),
                eq(dto.get(0).getGender()));
        verifyNoMoreInteractions(service);

        Pageable capturedVal = captor.getValue();

        assertAll(
                () -> assertEquals(0,capturedVal.getPageNumber()),
                () -> assertEquals(dto.size(), capturedVal.getPageSize()),
                () -> assertEquals(dto.size(), resultPage.size()),
                () -> assertEquals(0, resultPage.number()),
                () -> assertEquals(dto.size(), resultPage.totalElements()),
                () -> assertEquals(dto.size(),resultPage.totalPages())
        );

        assertThat(dto)
                .usingRecursiveComparison()
                .isEqualTo(resultPage.content());
    }

    @Test
    void testSaveValidUser() throws Exception {
        //given
        when(service.saveUser(any(UserDto.class))).thenReturn(user);

        //when
        steps.mvcPost(singleUserJson)
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
        MvcResult result = steps.mvcPost(singleInvalidUserJson)
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

        steps.mvcPut(100L, updatedUser)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.age").value(updatedUser.getAge()));

        verify(service, times(1)).updateUser(any(UserDto.class), eq(100L));
    }

    @Test
    void checkUpdateUserUsingInvalidDto() throws Exception {
        UserDto updatedUser = copyOf(user);
        updatedUser.setFullName("");

        //using PUT on user with ID 1 but that's not ideal. User might not exist and then the test will die
        MvcResult result = steps.mvcPut(1L, updatedUser)
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

        steps.mvcDelete(1L)
                .andExpect(status().isNoContent());
        //then

        verify(service, times(1)).deleteUser(eq(1L));
    }

    @Test
    void checkDeleteInvalidUser() throws Exception {
        //given
        doThrow(new UserNotFoundException(1L)).when(service).deleteUser(1L);
        //when
        MvcResult result = steps.mvcDelete(1L)
                .andExpect(i -> assertEquals(UserNotFoundException.class, Objects.requireNonNull(i.getResolvedException()).getClass()))
                .andReturn();

        //then
        ExceptionDto dto = mapper.readValue(result.getResponse().getContentAsString(), ExceptionDto.class);

        assertAll(
                () -> assertEquals(HttpStatus.NOT_FOUND.toString(), dto.statusCode().toString()),
                () -> assertEquals("Not found", dto.type()),
                () -> assertEquals("User was NOT found", dto.message()),
                () -> assertEquals(dto.exceptionMessage().get(0).message(), new UserNotFoundException(1L).getMessage())
        );

        verify(service, times(1)).deleteUser(1L);
    }

    //Checking this method by trying to perform a GET request without /users endpoint
    @Test
    void checkNoHandlerFound() throws Exception {
        //when
        steps.mvcGet(Endpoints.DUMMY_ENDPOINT)
                .andExpect(status().isNotFound())
                .andExpect(i -> assertInstanceOf(NoHandlerFoundException.class, i.getResolvedException()));
        //then
        verifyNoInteractions(service);
    }

}
