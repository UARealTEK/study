package org.example.study.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

//TODO: make sure that id is working correctly
@AllArgsConstructor
@Builder
@Getter
@Setter
public class BorrowRecordResponseDto {
   Long id;
   BookDto book;
   String userName;
   LocalDateTime borrowedAt;
   LocalDateTime returnedAt;
}
