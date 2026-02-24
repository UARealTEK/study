package org.example.study.service;

import lombok.extern.slf4j.Slf4j;
import org.example.study.DTOs.PageResponseDTO;
import org.example.study.enums.Gender;
import org.example.study.DTOs.UserDto;
import org.example.study.Entities.UserEntity;
import org.example.study.repository.UserRepository;
import org.example.study.util.Converters.UserMapper;
import org.example.study.util.Exceptions.CustomExceptions.UserNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class UserService {

    private final UserRepository repository;
    private final UserMapper mapper;

    public UserService(UserRepository repository, UserMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public PageResponseDTO<UserDto> getAllUsers(Pageable pageable) {
        Page<UserEntity> page = repository.findAll(pageable);
        if (page.isEmpty()) {
            throw new UserNotFoundException();
        } else {
            Page<UserDto> userDtoPage = page.map(mapper::toUserDto);
            return mapper.toPageObj(userDtoPage);
        }
    }

    public UserDto getUserByID(Long id) {
        UserEntity entity = repository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        return mapper.toUserDto(entity);
    }

    public UserDto saveUser(UserDto dto) {
        UserEntity entity = repository.save(mapper.toEntity(dto));
        return mapper.toUserDto(entity);
    }

    @Transactional
    public UserDto updateUser(UserDto body, Long id) {
        UserEntity entity = repository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        UserEntity updatedEntity = updateUserData(entity, body);
        return mapper.toUserDto(repository.save(updatedEntity));
    }

    public void deleteUser(Long id) {
        UserEntity entity = repository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        repository.deleteById(entity.getId());
    }

    private UserEntity updateUserData(UserEntity userToUpdate, UserDto body) {
        userToUpdate.setAge(body.getAge());
        userToUpdate.setGender(body.getGender());
        userToUpdate.setFullName(body.getFullName());
        return userToUpdate;
    }

    public PageResponseDTO<UserDto> findUserByAgeAndGender(Pageable page, Integer age, Gender gender) {
        Page<UserEntity> list = repository.findByAgeAndGender(page, age,gender);
        if (list.isEmpty()) {
            throw new UserNotFoundException();
        } else  {
            Page<UserDto> userDtoPage = list.map(mapper::toUserDto);
            return mapper.toPageObj(userDtoPage);
        }
    }
}
