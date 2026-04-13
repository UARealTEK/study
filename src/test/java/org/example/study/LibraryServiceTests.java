package org.example.study;

import org.example.study.Annotations.RandomBookEntity;
import org.example.study.Annotations.RandomPageImplObj;
import org.example.study.DTOs.BookDto;
import org.example.study.DTOs.Entities.BookEntity;
import org.example.study.DTOs.PageResponseDTO;
import org.example.study.Util.BaseLibraryServiceTest;
import org.example.study.enums.PageStrategyType;
import org.example.study.testData.DTOResolvers.RandomBookDtoResolver;
import org.example.study.testData.DTOResolvers.RandomBookEntityResolver;
import org.example.study.testData.PageResolvers.RandomPageImplResolver;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith({
        MockitoExtension.class,
        RandomPageImplResolver.class,
        RandomBookDtoResolver.class,
        RandomBookEntityResolver.class,
        }
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

    @Test
    void checkGetAllBooksWithNotNullArgs(@RandomPageImplObj(strategy = PageStrategyType.RANDOM, totalElements = 15) Page<BookEntity> entityPage) {
        //given
        Pageable pageable = entityPage.getPageable();
        String bookName = entityPage.getContent().getFirst().getName();
        String authorName = entityPage.getContent().getFirst().getAuthor();
        //when

            when(repository.findAll(anySpec(), eq(pageable)))
                    .thenReturn(entityPage);
        //then
        PageResponseDTO<BookDto> response = service.findAllBooks(pageable, bookName, authorName);

        verify(repository, times(1)).findAll(anySpec(), eq(pageable));

        assertAll(
                () -> assertEquals(response.content().getFirst().getName(), bookName),
                () -> assertEquals(response.content().getFirst().getAuthor(), authorName)
        );

        assertAll(
                () -> assertNotNull(response),
                () -> assertNotNull(response.content()),
                () -> assertEquals(response.size(), entityPage.getSize()),
                () -> assertEquals(response.number(), entityPage.getNumber()),
                () -> assertEquals(response.totalPages(), entityPage.getTotalPages()),
                () -> assertEquals(response.totalElements(),entityPage.getTotalElements())
        );
    }

    //TODO: complete it
    @Test
    void checkGetAllBooksWhenRepositoryIsEmpty(@RandomPageImplObj(strategy = PageStrategyType.EMPTY) Page<BookEntity> entityPage) {
        //given
        Pageable pageable = entityPage.getPageable();
        when(repository.findAll(anySpec(), eq(pageable))).thenReturn(entityPage);
        //when

        PageResponseDTO<BookDto> response = service.findAllBooks(pageable, null, null);
        //then
        verify(repository, times(1)).findAll(anySpec(), eq(pageable));

        assertAll(
                () -> assertTrue(response.content().isEmpty()),
                () -> assertEquals(response.number(), entityPage.getNumber()),
                () -> assertEquals(response.size(), entityPage.getSize()),
                () -> assertEquals(0, response.totalElements()),
                () -> assertEquals(0, response.totalPages())
        );
    }

    @Test
    void checkGetSingleBook(@RandomBookEntity BookEntity bookEntity) {
        //given
        when(repository.findById(eq(bookEntity.getId()))).thenReturn(Optional.of(bookEntity));
        //when
        BookDto result = service.findById(bookEntity.getId());
        //then

        verify(repository, times(1)).findById(eq(bookEntity.getId()));
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(result.getName(), bookEntity.getName()),
                () -> assertEquals(result.getAuthor(), bookEntity.getAuthor())
        );
    }

    @Test
    void checkSaveBook(@RandomBookEntity BookEntity bookEntity) {
        //given
        when(repository.save(any(BookEntity.class))).thenReturn(bookEntity);
        ArgumentCaptor<BookEntity> bookEntityCaptor = ArgumentCaptor.forClass(BookEntity.class);
        BookDto bookDto = bookMapper.toDto(bookEntity);

        //when
        BookDto result = service.saveBook(bookDto);
        //then
        verify(repository, times(1)).save(bookEntityCaptor.capture());
        BookEntity capturedVal = bookEntityCaptor.getValue();

        assertAll(
                () -> assertEquals(capturedVal.getName(), bookDto.getName()),
                () -> assertEquals(capturedVal.getAuthor(), bookDto.getAuthor())
        );

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(result.getName(), bookEntity.getName()),
                () -> assertEquals(result.getAuthor(), bookEntity.getAuthor())
        );
    }


}
