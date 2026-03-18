package org.example.study.util.Converters;

import org.example.study.DTOs.BookDto;
import org.example.study.DTOs.Entities.BookEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@SuppressWarnings("unused")
@Mapper(componentModel = "spring")
public interface BookMapper {

    @Mapping(target = "id", ignore = true)
    BookEntity toEntity(BookDto dto);

    BookDto toBookDto(BookEntity dto);
}
