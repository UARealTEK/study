package org.example.study.util.Exceptions.ExceptionHandler;

import org.springframework.http.HttpStatus;

import java.util.List;

public record ExceptionDto(
        HttpStatus statusCode,
        String type,
        String message,
        List<FieldErrorDto> exceptionMessage) {

    public static ExceptionDto of(ApiErrorType errorType, List<FieldErrorDto> exceptionMessage) {
        return new ExceptionDto(
                errorType.getStatus(),
                errorType.getType(),
                errorType.getMessage(),
                exceptionMessage
        );
    }
}
