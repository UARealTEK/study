package org.example.study.util.Converters;

import org.example.study.DTOs.BorrowRecordResponseDto;
import org.example.study.DTOs.Entities.BorrowRecordEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

//TODO: fix it after changing everything to unit context in tests
// provide proper mapping methods
@SuppressWarnings("unused")
@Mapper(componentModel = "spring", uses = {UserMapper.class, BookMapper.class})
public interface BorrowRecordMapper extends BaseMapper {

    @Mapping(source = "user", target = "userName")
    @Mapping(source = "book", target = "book")
    BorrowRecordResponseDto toDto(BorrowRecordEntity entity);
}
