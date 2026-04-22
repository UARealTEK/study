package org.example.study.util.Exceptions.CustomExceptions;

import lombok.Getter;

//TODO: add this exception to my ExceptionWorker
@Getter
@SuppressWarnings("unused")
public class BookAlreadyBorrowedException extends RuntimeException {

    private final String bookName;
    private final String bookAuthor;

    public BookAlreadyBorrowedException(String bookName, String bookAuthor) {
        super("The book with the following name -> " + bookName + " and author -> " + bookAuthor + " is already borrowed.");
        this.bookAuthor = bookAuthor;
        this.bookName = bookName;
    }

    public BookAlreadyBorrowedException() {
        super("Book already borrowed");
        this.bookName = null;
        this.bookAuthor = null;
    }

}
