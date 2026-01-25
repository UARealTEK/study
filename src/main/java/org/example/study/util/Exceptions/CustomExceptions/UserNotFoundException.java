package org.example.study.util.Exceptions.CustomExceptions;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(Long id) {
        super("The user with the following id -> " + id + " was not found");
    }

    public UserNotFoundException() {
        super("The users were not found");
    }

}
