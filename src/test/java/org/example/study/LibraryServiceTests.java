package org.example.study;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.example.study.Annotations.RandomBookDto;
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
import org.example.study.util.Exceptions.CustomExceptions.DuplicateBookException;
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

@Epic("Library Management")
@Feature("Book Service Operations")
@ExtendWith({
        MockitoExtension.class,
        RandomPageImplResolver.class,
        RandomBookDtoResolver.class,
        RandomBookEntityResolver.class,
        }
)
public class LibraryServiceTests extends BaseLibraryServiceTest {

    @Test
    @Story("Retrieve all books with pagination")
    @Description("Test retrieving all books from the library with pagination parameters. " +
            "Verifies that the service correctly maps entities to DTOs and returns proper page metadata.")
    @Severity(SeverityLevel.NORMAL)
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
    @Story("Retrieve books with filtering")
    @Description("Test retrieving books filtered by name and author parameters. " +
            "Verifies that the service applies filtering criteria correctly and returns matching results.")
    @Severity(SeverityLevel.NORMAL)
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

        verify(repository, times(1)).findAll(bookEntitySpecCaptor.capture(), eq(pageable));
        assertNotNull(bookEntitySpecCaptor.getValue());

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

    @Test
    @Story("Handle empty repository")
    @Description("Test behavior when the repository is empty. " +
            "Verifies that the service returns an empty page with correct metadata when no books exist.")
    @Severity(SeverityLevel.NORMAL)
    void checkGetAllBooksWhenRepositoryIsEmpty(@RandomPageImplObj(strategy = PageStrategyType.EMPTY) Page<BookEntity> entityPage) {
        //given
        Pageable pageable = entityPage.getPageable();
        when(repository.findAll(anySpec(), eq(pageable))).thenReturn(entityPage);
        //when

        PageResponseDTO<BookDto> response = service.findAllBooks(pageable, null, null);
        //then
        verify(repository, times(1)).findAll(bookEntitySpecCaptor.capture(), eq(pageable));
        //todo: is this really null? If there were NO name / author passed in - it will return unrestricted(). Can this be counted as null ?
        assertNull(bookEntitySpecCaptor.getValue());

        assertAll(
                () -> assertTrue(response.content().isEmpty()),
                () -> assertEquals(response.number(), entityPage.getNumber()),
                () -> assertEquals(response.size(), entityPage.getSize()),
                () -> assertEquals(0, response.totalElements()),
                () -> assertEquals(0, response.totalPages())
        );
    }

    @Test
    @Story("Retrieve single book by ID")
    @Description("Test retrieving a single book by its ID. " +
            "Verifies that the service correctly finds and maps the book entity to DTO when the book exists.")
    @Severity(SeverityLevel.NORMAL)
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
    @Story("Create new book")
    @Description("Test saving a new book to the library. " +
            "Verifies that the service correctly maps DTO to entity, saves it to repository, and returns the saved book as DTO.")
    @Severity(SeverityLevel.NORMAL)
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

    @Test
    @Story("Prevent duplicate book creation")
    @Description("Test error handling when attempting to save a book that already exists. " +
            "Verifies that the service throws DuplicateBookException and prevents duplicate entries.")
    @Severity(SeverityLevel.CRITICAL)
    void checkSaveExistingBook(@RandomBookDto BookDto dto) {
        //given
        when(repository.existsByNameAndAuthor(eq(dto.getName()), eq(dto.getAuthor()))).thenReturn(true);
        //when
        var ex = assertThrows(DuplicateBookException.class, () -> service.saveBook(dto));
        //then
        verify(repository, times(1)).existsByNameAndAuthor(eq(dto.getName()), eq(dto.getAuthor()));
        verifyNoMoreInteractions(repository);

        assertAll(
                () -> assertNotNull(ex),
                () -> assertInstanceOf(DuplicateBookException.class, ex),
                () -> assertEquals(new DuplicateBookException(dto.getName(), dto.getAuthor()).getMessage(), ex.getMessage())
        );
    }

    @Test
    @Story("Check book updating")
    @Description("Book update using provided bookDTO" +
            "Verifies that service correctly checks for existence of ANOTHER book with the same data and then " +
            "correctly updates EXISTING book entity with the provided data")
    @Severity(SeverityLevel.CRITICAL)
    void checkUpdateBook(@RandomBookEntity BookEntity bookEntity,@RandomBookDto BookDto dto) {
        //given
        when(repository.findById(eq(bookEntity.getId()))).thenReturn(Optional.of(bookEntity));
        when(repository.existsByNameAndAuthorAndIdNot(dto.getName(), dto.getAuthor(), bookEntity.getId())).thenReturn(false);
        //when
        var result = service.updateBook(bookEntity.getId(), dto);
        //then
        verify(repository).findById(eq(bookEntity.getId()));
        verify(repository).existsByNameAndAuthorAndIdNot(eq(dto.getName()), eq(dto.getAuthor()), eq(bookEntity.getId()));
        verify(repository, never()).save(any(BookEntity.class));

        assertAll(
                () -> assertEquals(result.getName(), dto.getName()),
                () -> assertEquals(result.getAuthor(), dto.getAuthor())
        );

        /*
        Makes sure that the ENTITY itself was mutated => update was done IN PLACE
         */
        assertAll(
                () -> assertEquals(bookEntity.getAuthor(), result.getAuthor()),
                () -> assertEquals(bookEntity.getName(), result.getName())
        );
    }

    @Test
    @Story("Check valid book deletion")
    @Description("Valid book deletion from the library by ID. " +
            "Verifies that the service checks for book existence and deletes it properly, interacting with the repository as expected.")
    @Severity(SeverityLevel.CRITICAL)
    void checkDeleteBook(@RandomBookEntity BookEntity entity) {
        //given
        ArgumentCaptor<Long> longCaptor = ArgumentCaptor.forClass(Long.class);
        when(repository.existsById(eq(entity.getId()))).thenReturn(true);
        //when
        service.deleteBook(entity.getId());
        //then
        verify(repository, times(1)).existsById(longCaptor.capture());
        verify(repository, times(1)).deleteById(longCaptor.capture());
        verifyNoMoreInteractions(repository);

        assertAll(
                () -> assertEquals(entity.getId(),longCaptor.getAllValues().getFirst()),
                () -> assertEquals(entity.getId(),longCaptor.getAllValues().getLast())
        );
    }


}
