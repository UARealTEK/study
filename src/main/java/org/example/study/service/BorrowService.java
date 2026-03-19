package org.example.study.service;

import lombok.RequiredArgsConstructor;
import org.example.study.DTOs.BorrowRecordResponseDto;
import org.example.study.DTOs.Entities.BookEntity;
import org.example.study.DTOs.Entities.BorrowRecordEntity;
import org.example.study.DTOs.Entities.UserEntity;
import org.example.study.DTOs.PageResponseDTO;
import org.example.study.repository.BorrowRecordsRepository;
import org.example.study.util.Converters.BorrowRecordMapper;
import org.example.study.util.Exceptions.CustomExceptions.BorrowRecordDoesntExistsException;
import org.example.study.util.Exceptions.CustomExceptions.BorrowRecordExistsException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.time.LocalDateTime.now;

@SuppressWarnings("unused")
@Service
@RequiredArgsConstructor
public class BorrowService {

    private final BookService bookService;
    private final UserService userService;
    private final BorrowRecordsRepository borrowRecordsRepository;
    private final BorrowRecordMapper borrowRecordMapper;

    public PageResponseDTO<BorrowRecordResponseDto> getAllBorrowRecords(Pageable pageable) {
        Page<BorrowRecordEntity> page = borrowRecordsRepository.findAll(pageable);
        Page<BorrowRecordResponseDto> pageDto = page.map(borrowRecordMapper::toDto);
        return borrowRecordMapper.toPageResponse(pageDto);
    }

    //TODO: add DB constraint ? to avoid race conditions
    // TODO: Read more about @Transactional
    //CREATE UNIQUE INDEX unique_active_borrow
    //ON borrow_record (book_id)
    //WHERE returned_at IS NULL;
    @Transactional
    public BorrowRecordResponseDto borrowBook(Long bookId, Long userID) {

        UserEntity user = userService.findEntityById(userID);
        BookEntity book = bookService.findEntityById(bookId);

        if (isBookBorrowed(book)) {
            throw new BorrowRecordExistsException(userID, bookId);
        }

        BorrowRecordEntity entity = borrowRecordsRepository.save(BorrowRecordEntity.builder()
                .user(user)
                .book(book)
                .borrowedAt(now())
                .build());

        return borrowRecordMapper.toDto(entity);
    }

    @Transactional
    public BorrowRecordResponseDto returnBook(Long bookId, Long userID) {
        BorrowRecordEntity borrowRecordEntity = getActiveBorrowOrThrow(userID, bookId);
        borrowRecordEntity.setReturnedAt(now());
        return borrowRecordMapper.toDto(borrowRecordEntity);
    }

    private boolean isBookBorrowed(BookEntity book) {
        return borrowRecordsRepository.existsByBookAndReturnedAtIsNull(book);
    }

    private BorrowRecordEntity getActiveBorrowOrThrow(Long userId, Long bookId) {
        UserEntity userEntity = userService.findEntityById(userId);
        BookEntity bookEntity = bookService.findEntityById(bookId);
        return borrowRecordsRepository.findByUserAndBookAndReturnedAtIsNull(userEntity, bookEntity)
                .orElseThrow(BorrowRecordDoesntExistsException::new);
    }

}