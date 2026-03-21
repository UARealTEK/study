package org.example.study.controller;

import lombok.AllArgsConstructor;
import org.example.study.DTOs.BookDto;
import org.example.study.DTOs.PageResponseDTO;
import org.example.study.service.BookService;
import org.example.study.service.BorrowService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//TODO:
// - Get All books
// - Get 'Active' books (currently borrowed)
// - Get 'Available' books (currently in library)
// - Get single book status (borrowed / not borrowed)


@RestController
@RequestMapping("/library")
@AllArgsConstructor
public class LibraryController {

    private final BookService bookService;
    private final BorrowService borrowService;

    @GetMapping
    public PageResponseDTO<BookDto> getAllBooks(@PageableDefault(size = 5) Pageable pageable) {
        return bookService.findAllBooks(pageable);
    }

    //TODO: finish it
    public PageResponseDTO<BookDto> getAvailableBooks(Pageable pageable) {
        return null;
    }

}
