package org.example.study.service;

import lombok.extern.slf4j.Slf4j;
import org.example.study.DTOs.PageResponseDTO;
import org.example.study.DTOs.UserPatchDto;
import org.example.study.enums.Gender;
import org.example.study.DTOs.UserDto;
import org.example.study.DTOs.Entities.UserEntity;
import org.example.study.repository.UserRepository;
import org.example.study.util.Converters.UserMapper;
import org.example.study.util.Exceptions.CustomExceptions.UserNotFoundException;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.example.study.util.Filtering.EntitySpecifications.byAllFields;

@Slf4j
@Service
@CacheConfig(cacheNames = "users")
public class UserService {

    private final UserRepository repository;
    private final UserMapper mapper;

    public UserService(UserRepository repository, UserMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public PageResponseDTO<UserDto> getAllUsers(Pageable pageable, Integer age, String fullName, Gender gender) {
        Specification<UserEntity> spec = byAllFields(age,fullName,gender);
        Page<UserEntity> page = repository.findAll(spec, pageable);
        Page<UserDto> userDtoPage = page.map(mapper::toUserDto);
        return mapper.toPageResponse(userDtoPage);
    }

    @Cacheable(key = "#id")
    public UserDto getUserByID(Long id) {
        UserEntity entity = repository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        return mapper.toUserDto(entity);
    }

    public UserDto saveUser(UserDto dto) {
        UserEntity entity = repository.save(mapper.toEntity(dto));
        return mapper.toUserDto(entity);
    }

    @CacheEvict(key = "#id")
    @Transactional
    public UserDto updateUser(UserDto body, Long id) {
        UserEntity entity = repository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        updateUserData(entity, body);
        return mapper.toUserDto(entity);
    }

    @CacheEvict(key = "#id")
    @Transactional
    public UserDto patchUser(UserPatchDto body, Long id) {
        UserEntity entity = repository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        patchUserData(entity, body);
        return mapper.toUserDto(entity);
    }

    @CacheEvict(key = "#id")
    public void deleteUser(Long id) {
        if (!repository.existsById(id)) {
            throw new UserNotFoundException(id);
        }
        repository.deleteById(id);
    }

    private void updateUserData(UserEntity userToUpdate, UserDto body) {
        userToUpdate.setAge(body.getAge());
        userToUpdate.setGender(body.getGender());
        userToUpdate.setFullName(body.getFullName());
    }

    private void patchUserData(UserEntity userToUpdate, UserPatchDto body) {
        if (body.getAge() != null) {
            userToUpdate.setAge(body.getAge());
        }
         if (body.getFullName() != null) {
             userToUpdate.setFullName(body.getFullName());
         }
         if (body.getGender() != null) {
             userToUpdate.setGender(body.getGender());
         }
    }
}
