package org.example.study.util.Converters;

import org.example.study.DTOs.BorrowRecordResponseDto;
import org.example.study.DTOs.Entities.BorrowRecordEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@SuppressWarnings("unused")
@Mapper(componentModel = "spring")
public interface BorrowRecordMapper extends BaseMapper {

    @Mapping(source = "user.fullName", target = "userName")
    BorrowRecordResponseDto toDto(BorrowRecordEntity dto);
}
