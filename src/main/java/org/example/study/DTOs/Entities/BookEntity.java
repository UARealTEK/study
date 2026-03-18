package org.example.study.DTOs.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class BookEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name;
    String author;

    @OneToMany(mappedBy = "book")
    List<BorrowRecordEntity> borrowRecords = new ArrayList<>();

}
