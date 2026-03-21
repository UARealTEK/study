package org.example.study.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Getter
@Setter
public class BorrowRecordResponseDto {
   BookDto book;
   String userName;
   LocalDateTime borrowedAt;
   LocalDateTime returnedAt;
}
