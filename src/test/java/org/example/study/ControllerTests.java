package org.example.study;

import org.example.study.Annotations.RandomPageResponseDto;
import org.example.study.Annotations.RandomUserDto;
import org.example.study.DTOs.PageResponseDTO;
import org.example.study.DTOs.UserDto;
import org.example.study.Util.BaseControllerTest;
import org.example.study.Annotations.Smoke;
import org.example.study.enums.Endpoints;
import org.example.study.enums.Gender;
import org.example.study.testData.RandomPageResponseDTOResolver;
import org.example.study.testData.RandomUserDtoResolver;
import org.example.study.util.Exceptions.CustomExceptions.UserNotFoundException;
import org.example.study.util.Exceptions.ExceptionHandler.ExceptionDto;
import org.example.study.util.Exceptions.ExceptionHandler.FieldErrorDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
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
import static org.example.study.testData.TestData.getValidUsersWithFixedValues;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//TODO: adapt using @Nested for better structure of tests. For example, group all tests related to GET /users/{id} in one nested class and so on
//TODO: make sure Im using Parameterized tests where possible to avoid code duplication and make tests more readable
@Smoke
@ExtendWith(
        {RandomUserDtoResolver.class,
        RandomPageResponseDTOResolver.class}
)
class ControllerTests extends BaseControllerTest {

    @Test
    void testFindAllUsers(@RandomPageResponseDto PageResponseDTO<UserDto> dto) throws Exception {
        //given
        when(service.getAllUsers(any(Pageable.class),
                isNull(),
                isNull(),
                isNull()))
                .thenReturn(dto);

        //when
        String usersJson = mapper.writeValueAsString(dto);
        steps.mvcGet()
                .andExpect(content().json(usersJson));
        //then
        verify(service, times(1)).getAllUsers(
                any(Pageable.class),
                isNull(),
                isNull(),
                isNull());
    }

    @Test
    void checkPagination(@RandomPageResponseDto PageResponseDTO<UserDto> dto) throws Exception {
        //when
        when(service.getAllUsers(
                any(Pageable.class),
                isNull(),
                isNull(),
                isNull())).thenReturn(dto);

        //then
        MvcResult result = steps.mvcGet(Map.of(
                "page", String.valueOf(dto.number()),
                "size", String.valueOf(dto.size()))
        ).andReturn();

        PageResponseDTO<UserDto> resultPage = mapper.readValue(
                result.getResponse().getContentAsString(), new TypeReference<>() {}
        );

        verify(service).getAllUsers(argThat(
                pageable -> {
                    assertThat(pageable.getPageNumber()).isEqualTo(dto.number());
                    assertThat(pageable.getPageSize()).isEqualTo(dto.size());
                    return true;
                }
        ),
                isNull(),isNull(),isNull());

        verify(service, times(1)).getAllUsers(any(Pageable.class),isNull(),isNull(),isNull());
        verifyNoMoreInteractions(service);

        assertThat(dto)
                .usingRecursiveComparison()
                .isEqualTo(resultPage);
    }

    @Test
    void testFindSingleValidUser(@RandomUserDto UserDto userDto) throws Exception {
        //given
        when(service.getUserByID(any(Long.class))).thenReturn(userDto);

        //when
        MvcResult result = steps.mvcGet(1L)
                .andExpect(status().isOk())
                .andReturn();

        UserDto resultDto = mapper.readValue(result.getResponse().getContentAsString(), UserDto.class);

        //then
        verify(service, times(1)).getUserByID(any(Long.class));
        verifyNoMoreInteractions(service);
        assertThat(resultDto)
                .usingRecursiveComparison()
                .isEqualTo(userDto);
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
    @ValueSource(ints = {1, 3}) // TODO: use ParameterResolver here instead of ValueSource
    void testFindSingleValidUserUsingParams(int count) throws Exception {
        //given
        List<UserDto> dto = getValidUsersWithFixedValues(count);
        Map<String,String> params = Map.of(
                "age", String.valueOf(dto.get(0).getAge()),
                "fullName", dto.get(0).getFullName(),
                "gender", dto.get(0).getGender().name().toLowerCase(),
                "page", String.valueOf(0),
                "size", String.valueOf(dto.size())
        );

        Page<UserDto> page = new PageImpl<>(dto, PageRequest.of(0,dto.size()), dto.size());

        //when
        when(service.getAllUsers(any(Pageable.class)
                ,anyInt()
                ,anyString()
                ,any(Gender.class))).thenReturn(userMapper.toPageResponse(page));

        //then
        MvcResult result = steps.mvcGet(params)
                .andExpect(status().isOk())
                .andReturn();

        PageResponseDTO<UserDto> resultPage = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
        ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);

        verify(service).getAllUsers(
                captor.capture(),
                eq(dto.get(0).getAge()),
                eq(dto.get(0).getFullName()),
                eq(dto.get(0).getGender()));
        verifyNoMoreInteractions(service);

        Pageable capturedVal = captor.getValue();

        assertAll(
                () -> assertEquals(0,capturedVal.getPageNumber()), // checking that page number is correctly passed to service layer.
                () -> assertEquals(dto.size(), capturedVal.getPageSize()), // checking that amount of items that can be placed on one page is correctly passed to service layer
                () -> assertEquals(dto.size(), resultPage.size()), // checking that actual size of the page that is returned to the user is correct
                () -> assertEquals(0, resultPage.number()), // checking that page number in the response is correct
                () -> assertEquals(dto.size(), resultPage.totalElements()) // checking that total amount of items that can be paginated is correct
        );

        assertThat(dto)
                .usingRecursiveComparison()
                .isEqualTo(resultPage.content());
    }

    @Test
    void testSaveValidUser(@RandomUserDto UserDto dto) throws Exception {
        //given
        when(service.saveUser(any(UserDto.class))).thenReturn(dto);

        //when
        steps.mvcPost(singleUserJson) // TODO: I can probably rewrite mvcPost method to accept Object directly instead of JSON
                .andExpect(status().isCreated())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(singleUserJson)); // TODO: Extract the JSON from the passed in Object to further assert it
        //then

        //Tried to use captor here to see how it works. basically it tracks the argument that crossed from controller to service
        //Then I can inspect what DTO came to the service. Check if it matches the DTO that I'm intended to use further
        ArgumentCaptor<UserDto> captor = ArgumentCaptor.forClass(UserDto.class);
        verify(service).saveUser(captor.capture());
        UserDto dtoCaptor = captor.getValue();

        //checks to verify that DTO which was serialized from JSON and passed to the service layer contains correct data
        assertAll(
                () -> assertEquals(dtoCaptor.getAge(), dto.getAge()),
                () -> assertEquals(dtoCaptor.getGender(), dto.getGender()),
                () -> assertEquals(dtoCaptor.getFullName(), dto.getFullName())
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
        MvcResult result = steps.mvcPost(singleInvalidUserJson) //TODO: use ParameterResolver here instead of singleInvalidUserJson. Pass in the object and then extract JSON from it
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

    //TODO: don't use copyOf if not needed
    @Test
    void checkValidUpdateUser(@RandomUserDto UserDto dto) throws Exception {
        UserDto updatedUser = copyOf(dto);
        updatedUser.setAge(dto.getAge() + 10);

        when(service.updateUser(any(UserDto.class), eq(100L))).thenReturn(updatedUser);

        MvcResult result = steps.mvcPut(100L, updatedUser)
                .andExpect(status().isOk())
                .andReturn();

        UserDto resultDto = mapper.readValue(result.getResponse().getContentAsString(), UserDto.class);

        verify(service, times(1)).updateUser(any(UserDto.class), eq(100L));
        assertNotEquals(resultDto.getAge(), dto.getAge());
        assertEquals(resultDto.getAge(), updatedUser.getAge());
    }

    //TODO: don't use copyOf if not needed
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
