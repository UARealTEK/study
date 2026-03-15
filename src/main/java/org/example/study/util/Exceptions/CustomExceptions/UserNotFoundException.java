package org.example.study.util.Exceptions.CustomExceptions;

import lombok.Getter;

@Getter
@SuppressWarnings("unused")
public class UserNotFoundException extends RuntimeException {

    private final Long id;

    public UserNotFoundException(Long id) {
        super("The user with the following id -> " + id + " was not found");
        this.id = id;
    }

    public UserNotFoundException() {
        super("The users were not found");
        this.id = null;
    }

}
