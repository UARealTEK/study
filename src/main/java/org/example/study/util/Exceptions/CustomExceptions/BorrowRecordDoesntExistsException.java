package org.example.study.util.Exceptions.CustomExceptions;

import lombok.Getter;

@Getter
@SuppressWarnings("unused")
public class BorrowRecordDoesntExistsException extends RuntimeException {

    private final Long userId;
    private final Long bookId;

    public BorrowRecordDoesntExistsException(Long userId, Long bookId) {
        super("The record with the following Book id -> " + bookId + " and User -> " + userId + " doesnt exists.");
        this.userId = userId;
        this.bookId = bookId;
    }

    public BorrowRecordDoesntExistsException() {
        super("The record doesnt exists.");
        this.userId = null;
        this.bookId = null;
    }

}
