package org.example.study.util.Exceptions.ExceptionHandler;

public record FieldErrorDto(String error, String message) {

    public static FieldErrorDto of(String error, String message) {
        return new FieldErrorDto(error,message);
    }
}