package org.example.study.util.Exceptions.ExceptionHandler;

import org.springframework.http.HttpStatusCode;

import java.util.List;

public record ExceptionDto(HttpStatusCode statusCode, String type, String message, List<FieldErrorDto> exceptionMessage) {

}
