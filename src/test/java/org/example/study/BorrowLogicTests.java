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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

//todo: start working on it
@Smoke
@Epic("Library Management")
@Feature("Book Service Operations")
@ExtendWith({
        MockitoExtension.class,
        RandomPageResponseDTOResolver.class,
        RandomBorrowRecordDtoResolver.class,
        RandomPageImplResolver.class
})
public class BorrowLogicTests extends BaseBorrowingServiceTest {

    //TODO: this needs some work on Mappers first (changing to Spring everywhere)
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
        verify(repository).findAll(any(Pageable.class));
        verifyNoMoreInteractions(repository);
        assertAll(
                () -> assertEquals(response.size(), page.getSize()),
                () -> assertEquals(response.number(), page.getNumber()),
                () -> assertEquals(response.totalElements(), page.getTotalElements()),
                () -> assertEquals(response.totalPages(), page.getTotalPages())
        );

        for (int i = 0; i < response.content().size(); i++) {
            assertEquals(response.content().get(i).getId(), page.getContent().get(i).getId());
            assertEquals(response.content().get(i).getUserName(), page.getContent().get(i).getUser().getFullName());
            assertEquals(response.content().get(i).getBook().getAuthor(), page.getContent().get(i).getBook().getAuthor());
            assertEquals(response.content().get(i).getBook().getName(), page.getContent().get(i).getBook().getName());
            assertEquals(response.content().get(i).getBorrowedAt(), page.getContent().get(i).getBorrowedAt());
            assertEquals(response.content().get(i).getReturnedAt(), page.getContent().get(i).getReturnedAt());
        }

        List<BorrowRecordResponseDto> borrowRecordResponseDtoList = page.getContent().stream().map(borrowRecordMapper::toDto).toList();
        assertThat(response.content().size()).isEqualTo(borrowRecordResponseDtoList.size());
        assertThat(response.content()).usingRecursiveFieldByFieldElementComparator().isEqualTo(borrowRecordResponseDtoList);
    }
}
