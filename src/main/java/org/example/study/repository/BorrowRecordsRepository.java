package org.example.study.repository;

import org.example.study.DTOs.Entities.BookEntity;
import org.example.study.DTOs.Entities.BorrowRecordEntity;
import org.example.study.DTOs.Entities.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@SuppressWarnings("unused")
@Repository
public interface BorrowRecordsRepository extends JpaRepository<BorrowRecordEntity, Long> {
    boolean existsByBookAndReturnedAtIsNull(BookEntity book);

    boolean existsByUserAndReturnedAtIsNull(UserEntity user);

    boolean existsByUserAndBook(UserEntity user, BookEntity book);

    Optional<BorrowRecordEntity> findByUserAndBookAndReturnedAtIsNull(UserEntity user, BookEntity book);

    Page<BorrowRecordEntity> findByReturnedAtIsNull(Pageable pageable);

    Page<BorrowRecordEntity> findByReturnedAtIsNotNull(Pageable pageable);
}
