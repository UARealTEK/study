package org.example.study;

import io.qameta.allure.*;
import org.example.study.Annotations.RandomPageImplObj;
import org.example.study.Annotations.Smoke;
import org.example.study.BaseTestPages.BaseBorrowingServiceTest;
import org.example.study.DTOs.BorrowRecordResponseDto;
import org.example.study.DTOs.Entities.BorrowRecordEntity;
import org.example.study.DTOs.PageResponseDTO;
import org.example.study.enums.PageStrategyType;
import org.example.study.testData.DTOResolvers.RandomBorrowRecordDtoResolver;
import org.example.study.testData.PageResolvers.RandomPageImplResolver;
import org.example.study.testData.PageResolvers.RandomPageResponseDTOResolver;
import org.example.study.util.Converters.BookMapperImpl;
import org.example.study.util.Converters.BorrowRecordMapperImpl;
import org.example.study.util.Converters.UserMapperImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Smoke
@Epic("Library Management")
@Feature("Book Service Operations")
@ExtendWith({
        SpringExtension.class,
        MockitoExtension.class,
        RandomPageResponseDTOResolver.class,
        RandomBorrowRecordDtoResolver.class,
        RandomPageImplResolver.class
})
@ContextConfiguration(classes = {
        BookMapperImpl.class,
        UserMapperImpl.class,
        BorrowRecordMapperImpl.class,
        ObjectMapper.class
})
public class BorrowLogicTests extends BaseBorrowingServiceTest {

    @Test
    @Story("Retrieve All records")
    @Description("Test aimed to check that service is able to fetch all available borrow records ")
    @Severity(SeverityLevel.CRITICAL)
    void checkGetAllBorrowRecords(@RandomPageImplObj(strategy = PageStrategyType.RANDOM, totalElements = 5) Page<BorrowRecordEntity> page) {
        //given
        Pageable pageable = page.getPageable();
        //when
        when(repository.findAll(any(Pageable.class))).thenReturn(page);
        PageResponseDTO<BorrowRecordResponseDto> response = service.getAllBorrowRecords(pageable);
        //then
        verify(repository).findAll(pageableArgumentCaptor.capture());
        verifyNoMoreInteractions(repository);

        Pageable usedPageable = pageableArgumentCaptor.getValue();

        //check that the passed in Pageable object was actually used by repository
        assertAll(
                () -> assertEquals(usedPageable.getPageNumber(), pageable.getPageNumber()),
                () -> assertEquals(usedPageable.getPageSize(), pageable.getPageSize()),
                () -> assertEquals(usedPageable.getSort(), pageable.getSort())
        );

        //check that response object page data matches with the mocked data which was specified in my condition
        assertAll(
                () -> assertEquals(response.size(), page.getSize()),
                () -> assertEquals(response.number(), page.getNumber()),
                () -> assertEquals(response.totalElements(), page.getTotalElements()),
                () -> assertEquals(response.totalPages(), page.getTotalPages())
        );

        List<BorrowRecordResponseDto> borrowRecordResponseDtoList = page.getContent().stream().map(borrowRecordMapper::toDto).toList();

        //check that the resulting content data is matched with the passed in mock data
        assertAll(
                () -> assertThat(response.content().size()).isEqualTo(borrowRecordResponseDtoList.size()),
                () -> assertThat(response.content())
                        .usingRecursiveFieldByFieldElementComparator()
                        .isEqualTo(borrowRecordResponseDtoList)
        );
    }
}
