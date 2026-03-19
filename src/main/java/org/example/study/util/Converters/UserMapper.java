package org.example.study.util.Converters;

import org.example.study.DTOs.UserDto;
import org.example.study.DTOs.Entities.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@SuppressWarnings("unused")
@Mapper(componentModel = "spring")
public interface UserMapper extends BaseMapper {

    @Mapping(target = "id", ignore = true)
    UserEntity toEntity(UserDto dto);

    UserDto toDto(UserEntity dto);
}
