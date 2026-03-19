package org.example.study.util.Converters;

import org.example.study.DTOs.BorrowRecordResponseDto;
import org.example.study.DTOs.Entities.BorrowRecordEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@SuppressWarnings("unused")
@Mapper(componentModel = "spring")
public interface BorrowRecordMapper {

    @Mapping(target = "id", ignore = true)
    BorrowRecordEntity toEntity(BorrowRecordResponseDto dto);

    BorrowRecordResponseDto toDto(BorrowRecordEntity dto);
}
