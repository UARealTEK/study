package org.example.study.util.Exceptions.CustomExceptions;

import lombok.Getter;

@Getter
@SuppressWarnings("unused")
public class DuplicateBookException extends RuntimeException {

    private final String name;
    private final String author;

    public DuplicateBookException(String bookName, String bookAuthor) {
        super("The book with the following name -> " + bookName + " and author -> " + bookAuthor + " is already exists.");
        this.author = bookAuthor;
        this.name = bookName;
    }

    public DuplicateBookException() {
        super("Book already exists in library");
        this.name = null;
        this.author = null;
    }

}
