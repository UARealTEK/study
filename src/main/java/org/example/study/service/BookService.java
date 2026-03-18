package org.example.study.service;

import lombok.RequiredArgsConstructor;
import org.example.study.DTOs.BookDto;
import org.example.study.DTOs.Entities.BookEntity;
import org.example.study.repository.BookRepository;
import org.example.study.util.Converters.BookMapper;
import org.example.study.util.Exceptions.CustomExceptions.BookNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

//TODO: create new Exceptions and probably new RestExceptionAdvice ?
@Service
@RequiredArgsConstructor
public class BookService {

    private BookRepository bookRepository;
    private final BookMapper mapper;

    //TODO: for now - Im not doing Working with specifications / Caches / Specific ResponseDTOs which include Page<T> here.
    // May be added in further iterations
    public List<BookDto> findAllBooks() {
        List<BookEntity> bookEntities = bookRepository.findAll();
        return bookEntities.stream().map(mapper::toBookDto).toList();
    }

    //TODO: throw an exception instead of returning null
    public BookDto findById(Long id) {
        BookEntity entity = bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
        return mapper.toBookDto(entity);
    }

    //TODO: think if I really need to return here BookDto ?
    // if YES - can I return the same stuff that came in to this service?
    public BookDto saveBook(BookDto dto) {
        BookEntity userEntity = bookRepository.save(mapper.toEntity(dto));
        return mapper.toBookDto(userEntity);
    }

    public BookDto updateBook(Long bookId, BookDto bookBody) {
        BookEntity entity = bookRepository.findById(bookId).orElseThrow(BookNotFoundException::new);
        updateBookData(entity, bookBody);
        bookRepository.save(entity);
        return mapper.toBookDto(entity);
    }

    /*
    actually believe I don't need it though but OK
     */
    private void updateBookData(BookEntity bookEntity, BookDto bookBody) {
        bookEntity.setAuthor(bookBody.getAuthor());
        bookEntity.setName(bookBody.getName());
    }




}
