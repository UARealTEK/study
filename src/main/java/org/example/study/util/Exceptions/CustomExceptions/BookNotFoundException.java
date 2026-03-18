package org.example.study.util.Exceptions.CustomExceptions;

import lombok.Getter;

@Getter
@SuppressWarnings("unused")
public class BookNotFoundException extends RuntimeException {

    private final Long id;

    public BookNotFoundException(Long id) {
        super("The book with the following id -> " + id + " was not found");
        this.id = id;
    }

    public BookNotFoundException() {
        super("The books were not found");
        this.id = null;
    }

}
