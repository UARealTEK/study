package org.example.study.util.Exceptions.ExceptionHandler;

import org.springframework.http.HttpStatusCode;

public record ExceptionDto(HttpStatusCode statusCode, String exceptionMessage) {
}
