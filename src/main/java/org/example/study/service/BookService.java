package org.example.study.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.study.DTOs.BookDto;
import org.example.study.DTOs.Entities.BookEntity;
import org.example.study.DTOs.PageResponseDTO;
import org.example.study.repository.BookRepository;
import org.example.study.util.Converters.BookMapper;
import org.example.study.util.Exceptions.CustomExceptions.BookNotFoundException;
import org.example.study.util.Exceptions.CustomExceptions.DuplicateBookException;
import org.example.study.util.Exceptions.CustomExceptions.IllegalRequestParameter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static org.example.study.util.Filtering.LibrarySpecification.byAllFields;


@SuppressWarnings("unused")
@Service
@RequiredArgsConstructor
public class BookService extends BaseService {

    private final BookRepository bookRepository;
    private final BookMapper mapper;

    public PageResponseDTO<BookDto> findAllBooks(Pageable pageable, String name, String author) {
        Specification<BookEntity> spec = byAllFields(name, author);
        Page<BookEntity> bookEntities = bookRepository.findAll(spec, normalizePageable(pageable));
        Page<BookDto> bookDtoPage = bookEntities.map(mapper::toDto);
        return mapper.toPageResponse(bookDtoPage);
    }

    public PageResponseDTO<BookDto> findAvailableBooks(Pageable pageable) {
        Page<BookEntity> bookEntities = bookRepository.findAvailableBooks(normalizePageable(pageable));
        Page<BookDto> bookDtoPage = bookEntities.map(mapper::toDto);
        return mapper.toPageResponse(bookDtoPage);
    }

    public PageResponseDTO<BookDto> findBorrowedBooks(Pageable pageable) {
        Page<BookEntity> bookEntities = bookRepository.findBorrowedBooks(normalizePageable(pageable));
        Page<BookDto> bookDtoPage = bookEntities.map(mapper::toDto);
        return mapper.toPageResponse(bookDtoPage);
    }

    public BookDto findById(Long id) {
        BookEntity entity = bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
        return mapper.toDto(entity);
    }

    public BookEntity findEntityById(Long id) {
        return bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
    }

    public BookDto findByNameAndAuthor(String bookName, String bookAuthor) {
        return bookRepository.findByNameAndAuthor(bookName, bookAuthor)
                .map(mapper::toDto)
                .orElseThrow(() -> new BookNotFoundException(bookName, bookAuthor));
    }

    public BookDto saveBook(BookDto dto) {
        if (isBookExists(dto)) {
            throw new DuplicateBookException(dto.getName(), dto.getAuthor());
        }
        return mapper.toDto(bookRepository.save(mapper.toEntity(dto)));
    }

    @Transactional
    public BookDto updateBook(Long bookId, BookDto bookBody) {
        BookEntity entity = bookRepository.findById(bookId).orElseThrow(BookNotFoundException::new);
        updateBookData(entity, bookBody);
        return mapper.toDto(entity);
    }

    @Transactional
    public BookDto patchBook(Long bookId, BookDto bookBody) {
        BookEntity entity = bookRepository.findById(bookId).orElseThrow(BookNotFoundException::new);
        patchBookData(entity, bookBody);
        return mapper.toDto(entity);
    }

    public void deleteBook(Long bookId) {
        if (!bookRepository.existsById(bookId)) {
            throw new BookNotFoundException(bookId);
        }
        bookRepository.deleteById(bookId);
    }

    /*
    actually believe I don't need it though but OK
     */
    private void updateBookData(BookEntity bookEntity, BookDto bookBody) {
        bookEntity.setAuthor(bookBody.getAuthor());
        bookEntity.setName(bookBody.getName());
    }

    private void patchBookData(BookEntity bookEntity, BookDto bookBody) {
        if (bookBody.getAuthor() != null) {
            bookEntity.setAuthor(bookBody.getAuthor());
        }

        if (bookBody.getName() != null) {
            bookEntity.setName(bookBody.getName());
        }
    }

    private boolean isBookExists(BookDto bookDto) {
        return bookRepository.existsByNameAndAuthor(bookDto.getName(), bookDto.getAuthor());
    }

    public void validateParameters(HttpServletRequest request) {
        Set<String> allowedParams = Set.of("name", "author", "page", "size", "sort");

        for (String allowedParam : request.getParameterMap().keySet()) {
            if (!allowedParams.contains(allowedParam)) {
                throw new IllegalRequestParameter(allowedParam);
            }
        }
    }


}
