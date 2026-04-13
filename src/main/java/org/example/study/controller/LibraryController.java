package org.example.study.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.study.DTOs.BookDto;
import org.example.study.DTOs.Models.BookRequestModel;
import org.example.study.DTOs.PageResponseDTO;
import org.example.study.service.BookService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

//TODO: introduce @ModelAttribute for parametrized requests
// This is done to get rid of request param validations (also think how to validate requests which are going to invalid params)
@RestController
@RequestMapping("/library")
@AllArgsConstructor
public class LibraryController {

    private final BookService bookService;

    @PostMapping(value = {"/", ""})
    public BookDto addBook(@Valid @RequestBody BookDto bookDto) {
        return bookService.saveBook(bookDto);
    }

    @GetMapping(value = {"/", ""})
    public PageResponseDTO<BookDto> getAllBooks(@PageableDefault(size = 5) Pageable pageable,
                                                @ModelAttribute @Valid BookRequestModel bookRequestModel
                                                ) {
        return bookService.findAllBooks(pageable, bookRequestModel.name(), bookRequestModel.author());
    }

    @GetMapping("/available")
    public PageResponseDTO<BookDto> getAvailableBooks(Pageable pageable) {
        return bookService.findAvailableBooks(pageable);
    }

    @GetMapping("/borrowed")
    public PageResponseDTO<BookDto> getBorrowedBooks(Pageable pageable) {
        return bookService.findBorrowedBooks(pageable);
    }

    @PutMapping("/{id}")
    public BookDto updateBook(@PathVariable Long id,@Valid @RequestBody BookDto bookDto) {
        return bookService.updateBook(id,bookDto);
    }

    @PatchMapping("/{id}")
    public BookDto patchBook(@PathVariable Long id,@Valid @RequestBody BookDto bookDto) {
        return bookService.patchBook(id,bookDto);
    }

    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
    }

}
