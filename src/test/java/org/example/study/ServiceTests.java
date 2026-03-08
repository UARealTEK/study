package org.example.study;

import org.example.study.Annotations.RandomUserDtoBody;
import org.example.study.DTOs.PageResponseDTO;
import org.example.study.enums.Gender;
import org.example.study.DTOs.UserDto;
import org.example.study.Entities.UserEntity;
import org.example.study.Util.BaseServiceTest;
import org.example.study.repository.UserRepository;
import org.example.study.service.UserService;
import org.example.study.Annotations.Unit;
import org.example.study.testData.RandomUserDtoResolver;
import org.example.study.util.Exceptions.CustomExceptions.UserNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
        RandomUserDtoResolver.class}
)
public class ServiceTests extends BaseServiceTest {

    @Mock
    public UserRepository repository;

    @InjectMocks
    public UserService service;

    @BeforeEach
    protected void init() {
        super.init();
        service = new UserService(repository, userMapper);
    }

    @AfterEach
    protected void cleanUp() {
        super.cleanUp();
        service = null;
    }

    @Test
    void checkGetAllUsers() {
        //given
        Pageable pageable = PageRequest.of(0,10);
        Page<UserEntity> mockedEntityPage = new PageImpl<>(users.getContent(), pageable, 50);

        //when
        when(repository.findAll(anySpec(),eq(pageable))).thenReturn(mockedEntityPage);

        PageResponseDTO<UserDto> result = service.getAllUsers(pageable, null,null,null);
        //then

        verify(repository, times(1)).findAll(anySpec(), eq(pageable));
        assertEquals(result.content().size(), users.getContent().size());
        assertEquals(0, result.number());
        assertEquals(10, result.size());
        assertEquals(50, result.totalElements());


        for (int i = 0; i < result.content().size(); i++) {
            assertEquals(result.content().get(i).getFullName(), users.getContent().get(i).getFullName());
            assertEquals(result.content().get(i).getAge(), users.getContent().get(i).getAge());
            assertEquals(result.content().get(i).getGender(), users.getContent().get(i).getGender());
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
    void checkUpdateUser(@RandomUserDtoBody UserDto dto) {
        when(repository.findById(anyLong())).thenReturn(Optional.of(user));

        //when
        UserDto returnedUser = service.updateUser(dto,user.getId());

        //then
        verify(repository, times(1)).findById(user.getId());
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

    @Test
    void checkValidSearchByAgeAndGender() {
        //given
        Pageable pageable = PageRequest.of(0,5);
        when(repository.findByAgeAndGender(any(Pageable.class), anyInt(), any(Gender.class))).thenReturn(users);

        //when
        /*
        whatever is passed inside as params -does not matter. Since we are returning stubbed value anyway
        As long as the datatype matches - its fine
         */
        PageResponseDTO<UserDto> list = service.findUserByAgeAndGender(pageable, 10,Gender.MALE);

        //then
        verify(repository, times(1)).findByAgeAndGender(pageable, 10, Gender.MALE);
        verifyNoMoreInteractions(repository);
        assertNotNull(list);
        assertFalse(list.content().isEmpty());
        assertEquals(list.content().size(), users.getContent().size());
    }

    @Test
    void checkNoMatchForAgeOrGender() {
        Pageable pageable = PageRequest.of(0,1);
        PageImpl<UserEntity> page = new PageImpl<>(List.of(), pageable, 0);
        //given
        when(repository.findByAgeAndGender(any(Pageable.class), anyInt(), any(Gender.class))).thenReturn(page);

        //when
        Exception ex = assertThrows(UserNotFoundException.class, () -> service.findUserByAgeAndGender(pageable, 1, Gender.FEMALE));

        //then
        verify(repository, times(1))
                .findByAgeAndGender(pageable,1, Gender.FEMALE);
        verifyNoMoreInteractions(repository);
        assertEquals(ex.getMessage(), new UserNotFoundException().getMessage());
        assertInstanceOf(UserNotFoundException.class, ex);
    }
}
