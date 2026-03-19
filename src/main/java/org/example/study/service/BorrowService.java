package org.example.study.service;

import lombok.RequiredArgsConstructor;
import org.example.study.DTOs.BorrowRecordResponseDto;
import org.example.study.DTOs.Entities.BookEntity;
import org.example.study.DTOs.Entities.BorrowRecordEntity;
import org.example.study.DTOs.Entities.UserEntity;
import org.example.study.repository.BorrowRecordsRepository;
import org.example.study.util.Exceptions.CustomExceptions.BorrowRecordExistsException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@SuppressWarnings("unused")
@Service
@RequiredArgsConstructor
public class BorrowService {

    private final BookService bookService;
    private final UserService userService;
    private final BorrowRecordsRepository borrowRecordsRepository;

    public BorrowRecordResponseDto borrowBook(Long bookId, Long userID) {

        UserEntity user = userService.findEntityById(userID);
        BookEntity book = bookService.findEntityById(bookId);

        if (isBookCurrentlyBorrowed(bookId)) {
            throw new BorrowRecordExistsException(userID, bookId);
        }

        borrowRecordsRepository.save(BorrowRecordEntity.builder()
                .user(user)
                .book(book)
                .borrowedAt(LocalDateTime.now())
                .build());

        return BorrowRecordResponseDto.builder()
                .userName(user.getFullName())
                .bookName(book.getName())
                .bookAuthor(book.getAuthor())
                .build();
    }


    private boolean isBookCurrentlyBorrowed(Long bookId) {
        BookEntity book = bookService.findEntityById(bookId);
        return borrowRecordsRepository.existsByBookAndReturnedAtIsNull(book);
    }


    //TODO: think about if I really need this validation ? extra logic, might be implemented later
    private boolean isUserCurrentlyBorrowing(Long userId) {
        UserEntity user = userService.findEntityById(userId);
        return borrowRecordsRepository.existsByUserAndReturnedAtIsNull(user);
    }

}