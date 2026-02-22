package org.example.study.service;

import lombok.extern.slf4j.Slf4j;
import org.example.study.enums.Gender;
import org.example.study.DTOs.UserDto;
import org.example.study.Entities.UserEntity;
import org.example.study.repository.UserRepository;
import org.example.study.util.Converters.Converter;
import org.example.study.util.Exceptions.CustomExceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.example.study.util.Converters.Converter.toEntity;

@Slf4j
@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public Page<UserDto> getAllUsers(Pageable pageable) {
        return repository.findAll(pageable).map(Converter::toUserDto);
    }

    public UserDto getUserByID(Long id) {
        UserEntity entity = repository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        return Converter.toUserDto(entity);
    }

    public UserDto saveUser(UserDto dto) {
        UserEntity entity = repository.save(toEntity(dto));
        return Converter.toUserDto(entity);
    }

    @Transactional
    public UserDto updateUser(UserDto body, Long id) {
        UserEntity entity = repository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        UserEntity updatedEntity = updateUserData(entity, body);
        return Converter.toUserDto(repository.save(updatedEntity));
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

    public Page<UserDto> findUserByAgeAndGender(Pageable page, Integer age, Gender gender) {
        Page<UserEntity> list = repository.findByAgeAndGender(page, age,gender);
        if (list.isEmpty()) {
            throw new UserNotFoundException();
        } else return list.map(Converter::toUserDto);
    }
}
