package org.example.study;

import org.example.study.DTOs.UserDto;
import org.example.study.DTOs.UserEntity;
import org.example.study.Util.BaseServiceTest;
import org.example.study.repository.UserRepository;
import org.example.study.service.UserService;
import org.example.study.util.Exceptions.CustomExceptions.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
        when(repository.findById(any(Long.class))).thenReturn(Optional.empty());

        //then
        UserNotFoundException ex = assertThrows(UserNotFoundException.class,
                () -> service.getUserByID(1L));

        assertEquals("The user with the following id -> " + 1L + " was not found", ex.getMessage());
        verify(repository, times(1)).findById(1L);
    }

    //rewrite using captors
    //learn how they work
    @Test
    void checkSaveUser() {
        //given
        when(repository.save(any(UserEntity.class))).thenReturn(user);
        //when

        UserDto userDto = service.saveUser(userCopy);
        //then

        verify(repository, times(1)).save(any(UserEntity.class));

        assertAll(
                () -> assertEquals(user.getAge(), userDto.getAge()),
                () -> assertEquals(user.getGender(), userDto.getGender()),
                () -> assertEquals(user.getFullName(), userDto.getFullName())
        );
    }



}
