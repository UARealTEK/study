package org.example.study.util.Converters;

import org.example.study.DTOs.UserDto;
import org.example.study.DTOs.UserEntity;

@SuppressWarnings("unused")
public class Converter {

    public static UserEntity toEntity(UserDto dto) {
        UserEntity userToReturn = new UserEntity();
        userToReturn.setAge(dto.getAge());
        userToReturn.setFullName(dto.getFullName());
        userToReturn.setGender(dto.getGender());
        return userToReturn;
    }

    public static UserDto toUserDto(UserEntity dto) {
        UserDto userToReturn = new UserDto();
        userToReturn.setAge(dto.getAge());
        userToReturn.setFullName(dto.getFullName());
        userToReturn.setGender(dto.getGender());
        return userToReturn;
    }
}
