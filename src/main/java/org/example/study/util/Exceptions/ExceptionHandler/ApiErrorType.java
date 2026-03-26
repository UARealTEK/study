package org.example.study.util.Exceptions.ExceptionHandler;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public enum ApiErrorType {
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "Validation error", "Incorrect body parameters provided"),
    MISSING_REQUEST_PARAM(HttpStatus.NOT_FOUND, "Validation error", "Missing request Parameters which are required"),
    BORROW_RECORD_NOT_FOUND(HttpStatus.NOT_FOUND, "Not Found", "Borrow Record for the specified parameters was not found"),
    BORROW_RECORD_ALREADY_EXISTS(HttpStatus.CONFLICT, "Duplicate Entry", "Borrow Record already exists for such parameters"),
    ILLEGAL_REQUEST_PARAMETER(HttpStatus.BAD_REQUEST, "Invalid Request", "Invalid query params were passed"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "Not found", "User was NOT found"),
    ARGUMENT_TYPE_MISMATCH(HttpStatus.BAD_REQUEST, "Argument mismatch", "Incorrect body arguments"),
    NO_HANDLER_FOUND(HttpStatus.NOT_FOUND, "No handler found", "Endpoint does not exist"),
    CONSTRAINT_VIOLATION(HttpStatus.BAD_REQUEST, "Constraint violation", "Constraint validation failed"),
    BOOK_NOT_FOUND(HttpStatus.NOT_FOUND, "Not found", "Book was not found"),
    DUPLICATE_BOOK(HttpStatus.CONFLICT, "Conflict", "Book with the same title and author already exists"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", "Something went wrong on the server side");

    @Getter
    private final HttpStatus status;
    @Getter
    private final String type;
    @Getter
    private final String message;

    ApiErrorType(HttpStatus status, String type, String message) {
        this.message = message;
        this.type = type;
        this.status = status;
    }

}
