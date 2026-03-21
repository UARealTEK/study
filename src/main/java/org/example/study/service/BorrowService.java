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
import org.springframework.dao.DataIntegrityViolationException;
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

    @Transactional
    public BorrowRecordResponseDto borrowBook(Long bookId, Long userID) {
        UserEntity user = userService.findEntityById(userID);
        BookEntity book = bookService.findEntityById(bookId);

        try {
            BorrowRecordEntity entity = borrowRecordsRepository.save(
                    BorrowRecordEntity.builder()
                            .user(user)
                            .book(book)
                            .borrowedAt(now())
                            .build()
            );

            return borrowRecordMapper.toDto(entity);
        } catch (DataIntegrityViolationException e) {
            throw new BorrowRecordExistsException(userID, bookId);
        }
    }

    @Transactional
    public BorrowRecordResponseDto returnBook(Long bookId, Long userID) {
        BorrowRecordEntity borrowRecordEntity = getActiveBorrowOrThrow(userID, bookId);
        borrowRecordEntity.setReturnedAt(now());
        return borrowRecordMapper.toDto(borrowRecordEntity);
    }

    public BorrowRecordResponseDto getRecordById(Long id) {
       return borrowRecordMapper.toDto(borrowRecordsRepository.findById(id)
               .orElseThrow(BorrowRecordDoesntExistsException::new));
    }

    private BorrowRecordEntity getActiveBorrowOrThrow(Long userId, Long bookId) {
        UserEntity userEntity = userService.findEntityById(userId);
        BookEntity bookEntity = bookService.findEntityById(bookId);
        return borrowRecordsRepository.findByUserAndBookAndReturnedAtIsNull(userEntity, bookEntity)
                .orElseThrow(BorrowRecordDoesntExistsException::new);
    }

}