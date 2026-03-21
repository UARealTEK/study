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

//TODO: Add following things:
// - Add BorrowStatus to BorrowRecordEntity

@RequestMapping("/borrow")
@RestController
@AllArgsConstructor
@Validated
public class BorrowController {

    private BorrowService borrowService;

    @PostMapping
    public BorrowRecordResponseDto borrowBook(@Valid @RequestBody BorrowRecordRequestDto requestDto) {
        return borrowService.borrowBook(requestDto.bookId(), requestDto.userId());
    }

    @PostMapping("/return")
    public BorrowRecordResponseDto returnBook(@Valid @RequestBody BorrowRecordRequestDto requestDto) {
        return borrowService.returnBook(requestDto.bookId(), requestDto.userId());
    }

    @GetMapping
    public PageResponseDTO<BorrowRecordResponseDto> getAllRecords(Pageable pageable) {
        return borrowService.getAllBorrowRecords(pageable);
    }

    @GetMapping("/{id}")
    public BorrowRecordResponseDto getBorrowRecordById(@PathVariable Long id) {
        return borrowService.getRecordById(id);
    }




}
