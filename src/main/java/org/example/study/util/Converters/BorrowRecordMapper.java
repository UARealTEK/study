package org.example.study.util.Converters;

import org.example.study.DTOs.BorrowRecordResponseDto;
import org.example.study.DTOs.Entities.BorrowRecordEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@SuppressWarnings("unused")
@Mapper(componentModel = "spring", uses = {BookMapper.class, UserMapper.class})
public interface BorrowRecordMapper extends BaseMapper {

    @Mapping(source = "user", target = "userName")
    BorrowRecordResponseDto toDto(BorrowRecordEntity dto);
}
