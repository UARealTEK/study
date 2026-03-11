package org.example.study.util.Converters;

import org.example.study.DTOs.PageResponseDTO;
import org.example.study.DTOs.UserDto;
import org.example.study.DTOs.Entities.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

@SuppressWarnings("unused")
@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    UserEntity toEntity(UserDto dto);

    UserDto toUserDto(UserEntity dto);

    default <T> PageResponseDTO<T> toPageResponse(Page<T> page){
        return new PageResponseDTO<>(
          page.getContent(),
          page.getNumber(),
          page.getSize(),
          page.getTotalElements(),
          page.getTotalPages()
        );
    }
}
