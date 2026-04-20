package org.example.study.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.study.DTOs.BorrowRecordRequestDto;
import org.example.study.DTOs.BorrowRecordResponseDto;
import org.example.study.DTOs.PageResponseDTO;
import org.example.study.service.BorrowService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @PatchMapping(value = {"/", ""})
    public BorrowRecordResponseDto returnBook(@Valid @RequestBody BorrowRecordRequestDto requestDto) {
        return borrowService.returnBook(requestDto.bookId(), requestDto.userId());
    }

    /*
    Fetch all available borrow records
    No query parameters are available for this record
    (Maybe I need to add it here. Not sure yet)
     */
    @GetMapping(value = {"/", ""})
    public PageResponseDTO<BorrowRecordResponseDto> getAllRecords(@PageableDefault(size = 5) Pageable pageable) {
        return borrowService.getAllBorrowRecords(pageable);
    }

    //GET borrow record by its ID
    @GetMapping("/{id}")
    public BorrowRecordResponseDto getBorrowRecordById(@PathVariable Long id) {
        return borrowService.getRecordById(id);
    }
}
