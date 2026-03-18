package org.example.study.DTOs.Entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;
/*
Represents an event. Meaning that user borrows a book at a certain time and returns it at another time
 */
@Entity
public class BorrowRecordEntity {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private BookEntity book;
    private LocalDateTime borrowedAt;
    private LocalDateTime returnedAt;
}
