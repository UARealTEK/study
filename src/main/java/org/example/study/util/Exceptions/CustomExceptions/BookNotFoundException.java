package org.example.study.util.Exceptions.CustomExceptions;

import lombok.Getter;

@Getter
@SuppressWarnings("unused")
public class BookNotFoundException extends RuntimeException {

    private final Long id;
    private final String name;
    private final String author;

    public BookNotFoundException(Long id) {
        super("The book with the following id -> " + id + " was not found");
        this.id = id;
        this.name = null;
        this.author = null;
    }

    public BookNotFoundException(String name, String author) {
        super("The book with the following name -> " + name + " and author -> " + author + " was not found");
        this.id = null;
        this.author = author;
        this.name = name;
    }

    public BookNotFoundException() {
        super("The books were not found");
        this.id = null;
        this.name = null;
        this.author = null;
    }

}
