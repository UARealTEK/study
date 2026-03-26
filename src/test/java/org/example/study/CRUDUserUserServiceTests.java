package org.example.study;

import io.qameta.allure.*;
import org.example.study.Annotations.*;
import org.example.study.DTOs.PageResponseDTO;
import org.example.study.DTOs.UserDto;
import org.example.study.DTOs.Entities.UserEntity;
import org.example.study.Util.BaseUserServiceTest;
import org.example.study.enums.PageStrategyType;
import org.example.study.testData.PageResolvers.RandomPageImplResolver;
import org.example.study.testData.DTOResolvers.RandomUserDtoResolver;
import org.example.study.testData.DTOResolvers.RandomUserEntityResolver;
import org.example.study.util.Exceptions.CustomExceptions.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Epic("User Management")
@Feature("User Service Logic")
@Unit
@ExtendWith(
        {
                MockitoExtension.class,
                RandomUserDtoResolver.class,
                RandomPageImplResolver.class,
                RandomUserEntityResolver.class
        }
)
public class CRUDUserUserServiceTests extends BaseUserServiceTest {

    @Test
    @Story("Retrieve All Users")
    @Description("Should retrieve all users from repository")
    void checkGetAllUsers(@RandomPageImplObj(strategy = PageStrategyType.RANDOM, totalElements = 15) Page<UserEntity> page) {
        //given
        Pageable pageable = page.getPageable();
        //when
        when(repository.findAll(anySpec(),eq(pageable))).thenReturn(page);

        PageResponseDTO<UserDto> result = service.getAllUsers(pageable, null,null,null);
        //then

        verify(repository, times(1)).findAll(anySpec(), eq(pageable));
        assertEquals(page.getContent().size(), result.content().size());
        assertEquals(pageable.getPageNumber(), result.number());
        assertEquals(pageable.getPageSize(), result.size());
        assertEquals(page.getTotalElements(), result.totalElements());

        for (int i = 0; i < result.content().size(); i++) {
            assertEquals(result.content().get(i).getFullName(), page.getContent().get(i).getFullName());
            assertEquals(result.content().get(i).getAge(), page.getContent().get(i).getAge());
            assertEquals(result.content().get(i).getGender(), page.getContent().get(i).getGender());
        }
    }

    @Test
    @Story("Filtered Retrieval")
    @Description("Should retrieve users with filters")
    void checkGetAllUsersParametrized(@RandomPageImplObj(strategy = PageStrategyType.SAME, totalElements = 10) Page<UserEntity> page) {
        //given
        Pageable pageable = page.getPageable();
        UserEntity dto = page.getContent().getFirst();

        //when
        when(repository.findAll(anySpec(),eq(pageable))).thenReturn(page);

        PageResponseDTO<UserDto> result = service.getAllUsers(pageable, dto.getAge(),dto.getFullName(),dto.getGender());
        //then

        verify(repository, times(1)).findAll(anySpec(), eq(pageable));
        assertEquals(page.getContent().size(), result.content().size());
        assertEquals(pageable.getPageNumber(), result.number());
        assertEquals(pageable.getPageSize(), result.size());
        assertEquals(page.getTotalElements(), result.totalElements());


        for (int i = 0; i < result.content().size(); i++) {
            assertEquals(result.content().get(i).getFullName(), page.getContent().get(i).getFullName());
            assertEquals(result.content().get(i).getAge(), page.getContent().get(i).getAge());
            assertEquals(result.content().get(i).getGender(), page.getContent().get(i).getGender());
        }
    }

    @Test
    @Story("Empty Results")
    @Description("Should handle empty repository")
    void checkGetAllUsersWhenRepositoryIsEmpty(@RandomPageImplObj(strategy = PageStrategyType.EMPTY) Page<UserEntity> page) {
        Pageable pageable = page.getPageable();

        //when
        when(repository.findAll(anySpec(),any(Pageable.class))).thenReturn(page);

        PageResponseDTO<UserDto> result = service.getAllUsers(pageable, null,null,null);

        //then
        verify(repository, times(1)).findAll(anySpec(),eq(pageable));
        assertThat(result.number()).isEqualTo(pageable.getPageNumber());
        assertThat(result.size()).isEqualTo(pageable.getPageSize());
        assertThat(result.totalElements()).isZero();
        assertThat(result.content()).isEmpty();
    }

    @Test
    @Story("Error Handling")
    @Description("Should throw exception for non-existent user")
    void checkUserNotFoundException() {
        //given
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        //then
        UserNotFoundException ex = assertThrows(UserNotFoundException.class,
                () -> service.findById(1L));

        assertEquals(ex.getMessage(), new UserNotFoundException(1L).getMessage());
        verify(repository, times(1)).findById(1L);
        assertInstanceOf(UserNotFoundException.class, ex);
    }

    @Test
    @Story("Retrieve Single User")
    @Description("Should retrieve a single user")
    void checkGetUser(@RandomUserEntity UserEntity entity) {
        when(repository.findById(anyLong())).thenReturn(Optional.of(entity));

        UserDto dto = service.findById(entity.getId());

        System.out.println(entity);
        System.out.println(dto);

        verify(repository, times(1)).findById(entity.getId());
        assertAll(
                () -> assertEquals(dto.getAge(), entity.getAge()),
                () -> assertEquals(dto.getGender(), entity.getGender()),
                () -> assertEquals(dto.getFullName(), entity.getFullName())
        );
    }

    @Test
    @Story("Create User")
    @Description("Should save a new user")
    void checkSaveUser(@RandomUserEntity UserEntity entity) {
        //given
        ArgumentCaptor<UserEntity> captor = ArgumentCaptor.forClass(UserEntity.class);
        when(repository.save(any(UserEntity.class))).thenReturn(entity);
        UserDto dto = userMapper.toUserDto(entity);

        //when
        UserDto userDto = service.saveUser(dto);

        verify(repository, times(1)).save(captor.capture());
        UserEntity captorValue = captor.getValue();

        //then
        assertAll(
                () -> assertEquals(captorValue.getFullName(), dto.getFullName()),
                () -> assertEquals(captorValue.getAge(), dto.getAge()),
                () -> assertEquals(captorValue.getGender(), dto.getGender()),
                () -> assertNull(captorValue.getId())
        );

        assertAll(
                () -> assertEquals(entity.getAge(), userDto.getAge()),
                () -> assertEquals(entity.getGender(), userDto.getGender()),
                () -> assertEquals(entity.getFullName(), userDto.getFullName())
        );
    }

    @Test
    @Story("Delete User")
    @Description("Should delete an existing user")
    void checkDeleteUser(@RandomUserEntity UserEntity entity) {
        //given
        ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
        when(repository.existsById(anyLong())).thenReturn(true);
        doNothing().when(repository).deleteById(anyLong());

        //when
        service.deleteUser(entity.getId());

        //then
        verify(repository, times(1)).existsById(captor.capture());
        verify(repository, times(1)).deleteById(captor.capture());
        verifyNoMoreInteractions(repository);

        assertAll(
                () -> assertEquals(captor.getAllValues().getFirst(), entity.getId()),
                () -> assertEquals(captor.getAllValues().get(1), entity.getId())
        );
    }

    @Test
    @Story("Error Handling")
    @Description("Should handle deletion of non-existent user")
    void checkInvalidDelete() {
        //given
        when(repository.existsById(anyLong())).thenReturn(false);

        //when
        Exception ex = assertThrows(UserNotFoundException.class, () -> service.deleteUser(1L));

        //then
        verify(repository, times(1)).existsById(eq(1L));
        verifyNoMoreInteractions(repository);
        assertEquals(ex.getMessage(), new UserNotFoundException(1L).getMessage());
        assertInstanceOf(UserNotFoundException.class, ex);
    }

    @Test
    @Story("Update User")
    @Description("Should update an existing user")
    void checkUpdateUser(@RandomUserDto UserDto dto, @RandomUserEntity UserEntity entity) {
        when(repository.findById(anyLong())).thenReturn(Optional.of(entity));

        //when
        UserDto returnedUser = service.updateUser(dto,entity.getId());

        //then
        verify(repository, times(1)).findById(entity.getId());
        verifyNoMoreInteractions(repository);

        //check that returned DTO matches with returned body entity
        assertAll(
                () -> assertEquals(returnedUser.getFullName(), dto.getFullName()),
                () -> assertEquals(returnedUser.getAge(), dto.getAge()),
                () -> assertEquals(returnedUser.getGender(), dto.getGender())
        );
    }

    @Test
    @Story("Error Handling")
    @Description("Should handle update of non-existent user")
    void checkInvalidUpdate(@RandomUserDto UserDto dto, @RandomUserEntity UserEntity entity) {
        //given
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        //when
        Exception ex = assertThrows(UserNotFoundException.class, () -> service.updateUser(dto, entity.getId()));

        //then
        verify(repository, times(1)).findById(entity.getId());
        verify(repository, never()).save(any());
        assertEquals(ex.getMessage(), new UserNotFoundException(entity.getId()).getMessage());
        assertInstanceOf(UserNotFoundException.class, ex);
    }
}
