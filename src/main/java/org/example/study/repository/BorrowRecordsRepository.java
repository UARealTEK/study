package org.example.study.repository;

import org.example.study.DTOs.Entities.BookEntity;
import org.example.study.DTOs.Entities.BorrowRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BorrowRecordsRepository extends JpaRepository<BorrowRecordEntity, Long> {

}
