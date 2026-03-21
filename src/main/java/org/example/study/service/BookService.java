package org.example.study.service;

import lombok.RequiredArgsConstructor;
import org.example.study.DTOs.BookDto;
import org.example.study.DTOs.BorrowRecordResponseDto;
import org.example.study.DTOs.Entities.BookEntity;
import org.example.study.DTOs.PageResponseDTO;
import org.example.study.repository.BookRepository;
import org.example.study.util.Converters.BookMapper;
import org.example.study.util.Exceptions.CustomExceptions.BookNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings("unused")
@Service
@RequiredArgsConstructor
public class BookService {

    private final BorrowService borrowService;
    private final BookRepository bookRepository;
    private final BookMapper mapper;

    //TODO: for now - Im not doing Working with specifications / Caches / Specific ResponseDTOs which include Page<T> here.
    // May be added in further iterations
    public PageResponseDTO<BookDto> findAllBooks(Pageable pageable) {
        Page<BookEntity> bookEntities = bookRepository.findAll(pageable);
        Page<BookDto> bookDtoPage = bookEntities.map(mapper::toDto);
        return mapper.toPageResponse(bookDtoPage);
    }

    public PageResponseDTO<BookDto> findAvailableBooks(Pageable pageable) {
        PageResponseDTO<BorrowRecordResponseDto> bookDtoPage = borrowService.getActiveBorrowRecords(pageable);
        return mapper.convertData(bookDtoPage, BorrowRecordResponseDto::getBook);
    }

    public BookDto findById(Long id) {
        BookEntity entity = bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
        return mapper.toDto(entity);
    }

    public BookEntity findEntityById(Long id) {
        return bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
    }

    //TODO: think if I really need to return here BookDto ?
    // if YES - can I return the same stuff that came in to this service?
    public BookDto saveBook(BookDto dto) {
        BookEntity userEntity = bookRepository.save(mapper.toEntity(dto));
        return mapper.toDto(userEntity);
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


}
