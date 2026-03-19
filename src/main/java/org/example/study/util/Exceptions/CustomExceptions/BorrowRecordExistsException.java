package org.example.study.util.Exceptions.CustomExceptions;

import lombok.Getter;

@Getter
@SuppressWarnings("unused")
public class BorrowRecordExistsException extends RuntimeException {

    private final Long userId;
    private final Long bookId;

    public BorrowRecordExistsException(Long userId,  Long bookId) {
        super("The record with the following Book id -> " + bookId + " and User -> " + userId + " is already exists.");
        this.userId = userId;
        this.bookId = bookId;
    }

    public BorrowRecordExistsException() {
        super("The record already exists.");
        this.userId = null;
        this.bookId = null;
    }

}
