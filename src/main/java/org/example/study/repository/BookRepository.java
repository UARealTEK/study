package org.example.study.repository;

import org.example.study.DTOs.Entities.BookEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, Long> {

    @Query("SELECT b FROM BookEntity b WHERE b.id NOT IN (SELECT br.book.id FROM BorrowRecordEntity br WHERE br.returnedAt IS NULL)")
    Page<BookEntity> findAvailableBooks(Pageable pageable);

    @Query("SELECT b FROM BookEntity b WHERE b.id IN (SELECT br.book.id FROM BorrowRecordEntity br WHERE br.returnedAt IS NULL)")
    Page<BookEntity> findBorrowedBooks(Pageable pageable);

}
