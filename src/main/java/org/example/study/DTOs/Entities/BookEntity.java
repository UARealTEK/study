package org.example.study.DTOs.Entities;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToMany;
import org.springframework.data.annotation.Id;

import java.util.List;

public class BookEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name;
    String author;

    @OneToMany(mappedBy = "bookEntity")
    List<BorrowRecordEntity> borrowRecords;


}
