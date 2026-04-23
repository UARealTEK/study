package org.example.study.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.study.DTOs.BorrowRecordRequestDto;
import org.example.study.DTOs.BorrowRecordResponseDto;
import org.example.study.DTOs.PageResponseDTO;
import org.example.study.service.BorrowService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RequestMapping("/borrows")
@RestController
@AllArgsConstructor
@Validated
public class BorrowController {

    private final BorrowService borrowService;

    @PostMapping(value = {"/", ""})
    public ResponseEntity<BorrowRecordResponseDto> borrowBook(@Valid @RequestBody BorrowRecordRequestDto requestDto) {
        BorrowRecordResponseDto responseDto = borrowService.borrowBook(requestDto.bookId(), requestDto.userId());
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(responseDto.getId())
                .toUri();
        return ResponseEntity.created(uri).body(responseDto);
    }

    @PatchMapping(value = {"/", ""})
    public ResponseEntity<BorrowRecordResponseDto> returnBook(@Valid @RequestBody BorrowRecordRequestDto requestDto) {
        BorrowRecordResponseDto responseDto = borrowService.returnBook(requestDto.bookId(), requestDto.userId());
        return ResponseEntity.ok().body(responseDto);
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
