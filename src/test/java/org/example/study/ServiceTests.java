package org.example.study;

import org.example.study.DTOs.UserDto;
import org.example.study.DTOs.UserEntity;
import org.example.study.Util.BaseServiceTest;
import org.example.study.repository.UserRepository;
import org.example.study.service.UserService;
import org.example.study.util.Exceptions.CustomExceptions.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ServiceTests extends BaseServiceTest {

    @Mock
    public UserRepository repository;

    @InjectMocks
    public UserService service;

    @Test
    void checkGetAllUsers() {
        //given
        when(repository.findAll()).thenReturn(users);
        //when

        List<UserDto> result = service.getAllUsers();
        //then

        verify(repository, times(1)).findAll();
        assertEquals(result.size(), users.size());

        for (int i = 0; i < result.size(); i++) {
            assertEquals(result.get(i).getFullName(), users.get(i).getFullName());
            assertEquals(result.get(i).getAge(), users.get(i).getAge());
            assertEquals(result.get(i).getGender(), users.get(i).getGender());
        }
    }

    @Test
    void checkGetAllUsersWhenRepositoryIsEmpty() {
        //given
        when(repository.findAll()).thenReturn(List.of());
        //when
        var result = service.getAllUsers();
        //then
        verify(repository, times(1)).findAll();
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void checkUserNotFoundException() {
        //given
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        //then
        UserNotFoundException ex = assertThrows(UserNotFoundException.class,
                () -> service.getUserByID(1L));

        assertEquals("The user with the following id -> " + 1L + " was not found", ex.getMessage());
        verify(repository, times(1)).findById(1L);
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
        doNothing().when(repository).deleteById(anyLong());

        //when

        //then
    }





}
