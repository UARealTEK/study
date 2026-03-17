package org.example.study.DTOs.Entities;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import java.time.LocalDateTime;
/*
Represents an event. Meaning that user borrows a book at a certain time and returns it at another time
 */
public class BorrowRecordEntity {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private UserEntity userEntity;

    @ManyToOne
    private BookEntity bookEntity;
    private LocalDateTime borrowedAt;
    private LocalDateTime returnedAt;
}
