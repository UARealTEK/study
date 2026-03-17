package org.example.study;

import org.example.study.Annotations.RandomInvalidUserDto;
import org.example.study.Annotations.RandomPageResponseDto;
import org.example.study.Annotations.RandomUserDto;
import org.example.study.DTOs.PageResponseDTO;
import org.example.study.DTOs.UserDto;
import org.example.study.Util.BaseControllerTest;
import org.example.study.Annotations.Smoke;
import org.example.study.enums.Endpoints;
import org.example.study.enums.Gender;
import org.example.study.enums.PageStrategyType;
import org.example.study.enums.UserDTOInvalidFlag;
import org.example.study.testData.DTOResolvers.RandomInvalidUserDtoResolver;
import org.example.study.testData.PageResolvers.RandomPageResponseDTOResolver;
import org.example.study.testData.DTOResolvers.RandomUserDtoResolver;
import org.example.study.util.Exceptions.CustomExceptions.UserNotFoundException;
import org.example.study.util.Exceptions.ExceptionHandler.ApiErrorType;
import org.example.study.util.Exceptions.ExceptionHandler.ExceptionDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.servlet.NoHandlerFoundException;
import tools.jackson.core.type.TypeReference;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.study.DTOs.UserDto.copyOf;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//TODO: adapt using @Nested for better structure of tests. For example, group all tests related to GET /users/{id} in one nested class and so on
// TODO: mark tests accordingly to the Allure report to make them pretty
@Smoke
@ExtendWith(
        {
                RandomUserDtoResolver.class,
                RandomPageResponseDTOResolver.class,
                RandomInvalidUserDtoResolver.class
        }
)
class CRUDControllerTests extends BaseControllerTest {

    @Test
    void testFindAllUsers(@RandomPageResponseDto(totalElements = 5) PageResponseDTO<UserDto> dto) throws Exception {
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
    void checkPagination(@RandomPageResponseDto(totalElements = 5) PageResponseDTO<UserDto> dto) throws Exception {
        //when
        when(service.getAllUsers(
                any(Pageable.class),
                isNull(),
                isNull(),
                isNull()))
                .thenReturn(dto);

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
                isNull(),
                isNull(),
                isNull()
        );

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
        doThrow(new UserNotFoundException(99L))
                .when(service)
                .getUserByID(99L);
        //when

        MvcResult result = steps.mvcGet(99L)
                .andExpect(status().isNotFound())
                .andReturn();
        //then
        ExceptionDto exceptionDto = mapper.readValue(result.getResponse().getContentAsString(), ExceptionDto.class);

        assertAll(
                () -> assertEquals(ApiErrorType.USER_NOT_FOUND.getStatus().toString(), exceptionDto.statusCode().toString()),
                () -> assertEquals(ApiErrorType.USER_NOT_FOUND.getType(), exceptionDto.type()),
                () -> assertEquals(ApiErrorType.USER_NOT_FOUND.getMessage(), exceptionDto.message()),
                () -> assertEquals(new UserNotFoundException(99L).getId().toString(), exceptionDto.exceptionMessage().getFirst().error()),
                () -> assertEquals(new UserNotFoundException(99L).getMessage(), exceptionDto.exceptionMessage().getFirst().message())
        );

        verify(service, times(1)).getUserByID(99L);
    }

    @Test
    void testFindValidUsersUsingParams(@RandomPageResponseDto(strategy = PageStrategyType.SAME, totalElements = 10) PageResponseDTO<UserDto> pageResponseDTO) throws Exception {
        //given
        List<UserDto> dto = pageResponseDTO.content();
        Map<String,String> params = Map.of(
                "age", String.valueOf(dto.getFirst().getAge()),
                "fullName", dto.getFirst().getFullName(),
                "gender", dto.getFirst().getGender().name().toLowerCase(),
                "page", String.valueOf(pageResponseDTO.number()),
                "size", String.valueOf(pageResponseDTO.size())
        );

        //when
        when(service.getAllUsers(any(Pageable.class)
                ,anyInt()
                ,anyString()
                ,any(Gender.class)))
                .thenReturn(pageResponseDTO);

        //then
        MvcResult result = steps.mvcGet(params)
                .andExpect(status().isOk())
                .andReturn();

        PageResponseDTO<UserDto> resultPage = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
        ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);

        verify(service).getAllUsers(
                captor.capture(),
                eq(dto.getFirst().getAge()),
                eq(dto.getFirst().getFullName()),
                eq(dto.getFirst().getGender()));
        verifyNoMoreInteractions(service);

        Pageable capturedVal = captor.getValue();

        assertAll(
                () -> assertEquals(pageResponseDTO.number(),capturedVal.getPageNumber()), // checking that page number is correctly passed to service layer.
                () -> assertEquals(pageResponseDTO.size(), capturedVal.getPageSize()) // checking that amount of items that can be placed on one page is correctly passed to service layer
        );

        assertThat(pageResponseDTO)
                .usingRecursiveComparison()
                .isEqualTo(resultPage);
    }

    @Test
    void testSaveValidUser(@RandomUserDto UserDto dto) throws Exception {
        //given
        when(service.saveUser(any(UserDto.class))).thenReturn(dto);


        //when
        steps.mvcPost(dto)
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(dto)));
        //then


        ArgumentCaptor<UserDto> captor = ArgumentCaptor.forClass(UserDto.class);
        verify(service).saveUser(captor.capture());
        UserDto dtoCaptor = captor.getValue();

        //checks to verify that DTO which was serialized from JSON and passed to the service layer contains correct data
        assertThat(dtoCaptor)
                .usingRecursiveComparison()
                .isEqualTo(dto);

        verifyNoMoreInteractions(service);
    }

    @Test
    void checkSaveInvalidUserValidation(@RandomInvalidUserDto(invalidFlag = UserDTOInvalidFlag.FULL_NAME) UserDto dto) throws Exception {
        //when
        MvcResult result = steps.mvcPost(dto)
                .andExpect(status().isBadRequest())
                .andExpect(i -> assertInstanceOf(MethodArgumentNotValidException.class, i.getResolvedException()))
                .andReturn();

        //then
        ExceptionDto exceptionDto = mapper.readValue(result.getResponse().getContentAsString(), ExceptionDto.class);

        assertAll(
                () -> assertEquals(ApiErrorType.VALIDATION_ERROR.getStatus().toString(), exceptionDto.statusCode().toString()),
                () -> assertEquals(ApiErrorType.VALIDATION_ERROR.getType(), exceptionDto.type()),
                () -> assertEquals(ApiErrorType.VALIDATION_ERROR.getMessage(), exceptionDto.message()),
                () -> assertNull(exceptionDto.exceptionMessage())
        );

        verifyNoInteractions(service);
    }

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
        assertAll(
                () -> assertEquals(resultDto.getAge(), updatedUser.getAge()),
                () -> assertEquals(resultDto.getFullName(), updatedUser.getFullName()),
                () -> assertEquals(resultDto.getGender(), updatedUser.getGender())
        );

    }

    @Test
    void checkUpdateUserUsingInvalidDto(@RandomInvalidUserDto(invalidFlag = UserDTOInvalidFlag.FULL_NAME) UserDto dto) throws Exception {

        //using PUT on user with ID 1 but that's not ideal. User might not exist and then the test will die
        MvcResult result = steps.mvcPut(1L, dto)
                .andExpect(status().isBadRequest())
                .andExpect(i -> assertInstanceOf(MethodArgumentNotValidException.class, i.getResolvedException()))
                .andReturn();

        ExceptionDto response = mapper.readValue(result.getResponse().getContentAsString(), ExceptionDto.class);
        assertAll(
                () -> assertEquals(ApiErrorType.VALIDATION_ERROR.getStatus().toString(), response.statusCode().toString()),
                () -> assertEquals(ApiErrorType.VALIDATION_ERROR.getType(), response.type()),
                () -> assertEquals(ApiErrorType.VALIDATION_ERROR.getMessage(), response.message()),
                () -> assertNull(response.exceptionMessage())
        );

        verifyNoInteractions(service);
    }

    @Test
    void checkDeleteValidUser() throws Exception {
        //given
        doNothing().when(service).deleteUser(1L);

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
                .andExpect(i -> assertInstanceOf(UserNotFoundException.class, i.getResolvedException()))
                .andReturn();

        //then
        ExceptionDto dto = mapper.readValue(result.getResponse().getContentAsString(), ExceptionDto.class);

        assertAll(
                () -> assertEquals(ApiErrorType.USER_NOT_FOUND.getStatus().toString(), dto.statusCode().toString()),
                () -> assertEquals(ApiErrorType.USER_NOT_FOUND.getType(), dto.type()),
                () -> assertEquals(ApiErrorType.USER_NOT_FOUND.getMessage(), dto.message()),
                () -> assertEquals(new UserNotFoundException(1L).getId().toString(), dto.exceptionMessage().getFirst().error()),
                () -> assertEquals(new UserNotFoundException(1L).getMessage(), dto.exceptionMessage().getFirst().message())
        );

        verify(service, times(1)).deleteUser(1L);
    }

    //Checking this method by trying to perform a GET request without /users endpoint
    @Test
    void checkNoHandlerFound() throws Exception {
        //when
        MvcResult result = steps.mvcGet(Endpoints.DUMMY_ENDPOINT)
                .andExpect(status().isNotFound())
                .andExpect(i -> assertInstanceOf(NoHandlerFoundException.class, i.getResolvedException()))
                        .andReturn();

        ExceptionDto exceptionDto = mapper.readValue(result.getResponse().getContentAsString(), ExceptionDto.class);
        //then

        assertAll(
                () -> assertEquals(ApiErrorType.NO_HANDLER_FOUND.getStatus().toString(), exceptionDto.statusCode().toString()),
                () -> assertEquals(ApiErrorType.NO_HANDLER_FOUND.getType(), exceptionDto.type()),
                () -> assertEquals(ApiErrorType.NO_HANDLER_FOUND.getMessage(), exceptionDto.message()),
                () -> assertNull(exceptionDto.exceptionMessage())
        );

        verifyNoInteractions(service);
    }

}
