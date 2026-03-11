package org.example.study;

import org.example.study.Annotations.PageImplObj;
import org.example.study.Annotations.RandomUserDto;
import org.example.study.Annotations.RandomUserEntity;
import org.example.study.DTOs.PageResponseDTO;
import org.example.study.DTOs.UserDto;
import org.example.study.DTOs.Entities.UserEntity;
import org.example.study.Util.BaseServiceTest;
import org.example.study.Annotations.Unit;
import org.example.study.testData.RandomPageImplResolver;
import org.example.study.testData.RandomUserDtoResolver;
import org.example.study.testData.RandomUserEntityResolver;
import org.example.study.util.Exceptions.CustomExceptions.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Unit
@ExtendWith(
        {MockitoExtension.class,
        RandomUserDtoResolver.class,
        RandomPageImplResolver.class,
        RandomUserEntityResolver.class}
)
public class ServiceTests extends BaseServiceTest {

    @Test
    void checkGetAllUsers(@PageImplObj Page<UserEntity> page) {
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
    void checkGetAllUsersParametrized(@PageImplObj(size = 10, page = 1) Page<UserEntity> page) {
        //given
        Pageable pageable = page.getPageable();
        UserEntity dto = page.getContent().get(0);

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
    void checkGetAllUsersWhenRepositoryIsEmpty() {
        //given
        Pageable pageable = PageRequest.of(0,1);
        Page<UserEntity> emptyPage = new PageImpl<>(List.of(), pageable, 0);

        //when
        when(repository.findAll(anySpec(),any(Pageable.class))).thenReturn(emptyPage);

        PageResponseDTO<UserDto> result = service.getAllUsers(pageable, null,null,null);

        //then
        verify(repository, times(1)).findAll(anySpec(),eq(pageable));
        assertThat(result.content()).isEmpty();
    }

    @Test
    void checkUserNotFoundException() {
        //given
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        //then
        UserNotFoundException ex = assertThrows(UserNotFoundException.class,
                () -> service.getUserByID(1L));

        assertEquals(ex.getMessage(), new UserNotFoundException(1L).getMessage());
        verify(repository, times(1)).findById(1L);
        assertInstanceOf(UserNotFoundException.class, ex);
    }

    @Test
    void checkGetUser() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(user));

        UserDto dto = service.getUserByID(user.getId());

        verify(repository, times(1)).findById(user.getId());
        assertAll(
                () -> assertEquals(dto.getAge(), user.getAge()),
                () -> assertEquals(dto.getGender(), user.getGender()),
                () -> assertEquals(dto.getFullName(), user.getFullName())
        );
    }

    @Test
    void checkSaveUser() {
        //given
        ArgumentCaptor<UserEntity> captor = ArgumentCaptor.forClass(UserEntity.class);
        when(repository.save(any(UserEntity.class))).thenReturn(user);

        //when
        UserDto userDto = service.saveUser(userCopy);

        verify(repository, times(1)).save(captor.capture());
        UserEntity captorValue = captor.getValue();

        //then
        // verifying that the DTO value which was passed to the service was mapped correctly to the entity which was passed further to the repository
        assertAll(
                () -> assertEquals(captorValue.getFullName(), userCopy.getFullName()),
                () -> assertEquals(captorValue.getAge(), userCopy.getAge()),
                () -> assertEquals(captorValue.getGender(), userCopy.getGender()),
                () -> assertNull(captorValue.getId())
        );


        //Verify that the entity which was returned by the mocked repository matches with the actual returned value returned by the service
        assertAll(
                () -> assertEquals(user.getAge(), userDto.getAge()),
                () -> assertEquals(user.getGender(), userDto.getGender()),
                () -> assertEquals(user.getFullName(), userDto.getFullName())
        );
    }

    @Test
    void checkDeleteUser() {
        //given
        ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
        when(repository.existsById(anyLong())).thenReturn(true);
        doNothing().when(repository).deleteById(anyLong());

        //when
        service.deleteUser(user.getId());

        //then
        verify(repository, times(1)).existsById(captor.capture());
        verify(repository, times(1)).deleteById(captor.capture());
        verifyNoMoreInteractions(repository);

        assertAll(
                () -> assertEquals(captor.getAllValues().get(0), user.getId()),
                () -> assertEquals(captor.getAllValues().get(1), user.getId())
        );
    }

    @Test
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

    /*
    Used ParameterResolver here just for the sake of testing how does it work
     */
    @Test
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
    void checkInvalidUpdate() {
        //given
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        //when
        Exception ex = assertThrows(UserNotFoundException.class, () -> service.updateUser(userCopy, user.getId()));

        //then
        verify(repository, times(1)).findById(user.getId());
        verify(repository, never()).save(any());
        assertEquals(ex.getMessage(), new UserNotFoundException(user.getId()).getMessage());
        assertInstanceOf(UserNotFoundException.class, ex);
    }
}
