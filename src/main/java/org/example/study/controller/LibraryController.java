package org.example.study.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.study.DTOs.BookDto;
import org.example.study.DTOs.PageResponseDTO;
import org.example.study.service.BookService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/library")
@AllArgsConstructor
public class LibraryController {

    private final BookService bookService;

    @PostMapping
    public BookDto addBook(@Valid @RequestBody BookDto bookDto) {
        return bookService.saveBook(bookDto);
    }

    @GetMapping("/all")
    public PageResponseDTO<BookDto> getAllBooks(@PageableDefault(size = 5) Pageable pageable) {
        return bookService.findAllBooks(pageable);
    }

    @GetMapping("/available")
    public PageResponseDTO<BookDto> getAvailableBooks(Pageable pageable) {
        return bookService.findAvailableBooks(pageable);
    }

    @GetMapping("/borrowed")
    public PageResponseDTO<BookDto> getBorrowedBooks(Pageable pageable) {
        return bookService.findBorrowedBooks(pageable);
    }

    @GetMapping("/{id}")
    public BookDto getSingleBookStatus(@PathVariable Long bookId) {
        return bookService.findById(bookId);
    }

}
