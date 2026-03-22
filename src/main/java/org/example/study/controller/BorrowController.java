package org.example.study.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.study.DTOs.BorrowRecordRequestDto;
import org.example.study.DTOs.BorrowRecordResponseDto;
import org.example.study.DTOs.PageResponseDTO;
import org.example.study.service.BorrowService;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


//TODO: currently - borrowing by bookId. But I never expose BookID directly.
// think about changing it to borrowing by bookName and bookAuthor.
@RequestMapping("/borrows")
@RestController
@AllArgsConstructor
@Validated
public class BorrowController {

    private final BorrowService borrowService;

    @PostMapping(value = {"/", ""})
    public BorrowRecordResponseDto borrowBook(@Valid @RequestBody BorrowRecordRequestDto requestDto) {
        return borrowService.borrowBook(requestDto.bookId(), requestDto.userId());
    }

    //TODO: think. May be it is better to just PATCH /borrows (and set returnedAt). But its fine for now
    @PostMapping("/return")
    public BorrowRecordResponseDto returnBook(@Valid @RequestBody BorrowRecordRequestDto requestDto) {
        return borrowService.returnBook(requestDto.bookId(), requestDto.userId());
    }

    @GetMapping(value = {"/", ""})
    public PageResponseDTO<BorrowRecordResponseDto> getAllRecords(Pageable pageable) {
        return borrowService.getAllBorrowRecords(pageable);
    }

    @GetMapping("/{id}")
    public BorrowRecordResponseDto getBorrowRecordById(@PathVariable Long id) {
        return borrowService.getRecordById(id);
    }
}
