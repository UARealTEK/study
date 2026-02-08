package org.example.study.service;

import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.example.study.DTOs.Gender;
import org.example.study.DTOs.UserDto;
import org.example.study.DTOs.UserEntity;
import org.example.study.repository.UserRepository;
import org.example.study.util.Converters.Converter;
import org.example.study.util.Exceptions.CustomExceptions.UserNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public List<UserDto> getAllUsers() {
        List<UserEntity> list = repository.findAll();
        return list.isEmpty() ?
                new ArrayList<>()
                : list.stream().map(Converter::toUserDto).toList();
    }

    public UserDto getUserByID(Long id) {
        UserEntity entity = repository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        return Converter.toUserDto(entity);
    }

    public UserDto saveUser(UserDto dto) {
        UserEntity entity = repository.save(Converter.toEntity(dto));
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

    public List<UserDto> findUserByAgeAndGender(Integer age, Gender gender) {
        List<UserEntity> list = repository.findByAgeAndGender(age,gender);
        if (list.isEmpty()) {
            throw new UserNotFoundException();
        } else return list.stream().map(Converter::toUserDto).toList();
    }
}
