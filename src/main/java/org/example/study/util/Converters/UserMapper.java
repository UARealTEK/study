package org.example.study.util.Converters;

import org.example.study.DTOs.UserDto;
import org.example.study.DTOs.Entities.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@SuppressWarnings("unused")
@Mapper(componentModel = "spring")
public interface UserMapper extends BaseMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "borrowRecords", ignore = true)
    @Mapping(source = "age", target = "age")
    @Mapping(source = "fullName", target = "fullName")
    @Mapping(source = "gender", target = "gender")
    UserEntity toEntity(UserDto dto);

    UserDto toDto(UserEntity dto);

    default String toUserName(UserEntity dto) {
        return dto.getFullName() != null ? dto.getFullName() : "Unknown User";
    }
}
