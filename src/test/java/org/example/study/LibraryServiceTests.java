package org.example.study;

import org.example.study.Annotations.RandomPageImplObj;
import org.example.study.DTOs.BookDto;
import org.example.study.DTOs.Entities.BookEntity;
import org.example.study.DTOs.PageResponseDTO;
import org.example.study.Util.BaseLibraryServiceTest;
import org.example.study.enums.PageStrategyType;
import org.example.study.testData.PageResolvers.RandomPageImplResolver;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(
        {MockitoExtension.class,
        RandomPageImplResolver.class}
)
public class LibraryServiceTests extends BaseLibraryServiceTest {

    @Test
    void checkGetAllBooks(@RandomPageImplObj(strategy = PageStrategyType.RANDOM, totalElements = 15) Page<BookEntity> entityPage) {
        //given
        Pageable pageable = entityPage.getPageable();

        //when
        when(repository.findAll(anySpec(), eq(pageable)))
                .thenReturn(entityPage);

        PageResponseDTO<BookDto> response = service.findAllBooks(pageable, null, null);
        //then

        verify(repository, times(1)).findAll(bookEntitySpecCaptor.capture(), eq(pageable));

        assertNotNull(bookEntitySpecCaptor.getValue());

        assertAll(
                () -> assertNotNull(response),
                () -> assertNotNull(response.content()),
                () -> assertEquals(response.size(), entityPage.getSize()),
                () -> assertEquals(response.number(), entityPage.getNumber()),
                () -> assertEquals(response.totalPages(), entityPage.getTotalPages()),
                () -> assertEquals(response.totalElements(),entityPage.getTotalElements())
        );
    }
}
