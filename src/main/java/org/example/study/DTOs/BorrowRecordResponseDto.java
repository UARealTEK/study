package org.example.study.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
public class BorrowRecordResponseDto {
   String bookAuthor;
   String bookName;
   String userName;
   LocalDateTime borrowedAt;
   LocalDateTime returnedAt;
}
