package org.example.study.util.Converters;

import org.example.study.DTOs.BorrowRecordResponseDto;
import org.example.study.DTOs.Entities.BorrowRecordEntity;
import org.mapstruct.Mapper;

@SuppressWarnings("unused")
@Mapper(componentModel = "spring")
public interface BorrowRecordMapper extends BaseMapper {

    BorrowRecordResponseDto toDto(BorrowRecordEntity dto);
}
