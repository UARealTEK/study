package org.example.study.util.Exceptions.ExceptionHandler;

import jakarta.validation.ConstraintViolationException;
import org.example.study.util.Exceptions.CustomExceptions.BookNotFoundException;
import org.example.study.util.Exceptions.CustomExceptions.UserNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.List;

@SuppressWarnings("unused")
@RestControllerAdvice
public class ExceptionWorker {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionDto> handleBodyException(MethodArgumentNotValidException e) {
        ApiErrorType errorType = ApiErrorType.VALIDATION_ERROR;
        return exceptionResponseBuilder(errorType, null);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionDto> handleGetNoUsersException(UserNotFoundException e) {
        ApiErrorType errorType = ApiErrorType.USER_NOT_FOUND;
        List<FieldErrorDto> errorDtoList = List.of(new FieldErrorDto(e.getId().toString(), e.getMessage()));
        return exceptionResponseBuilder(errorType, errorDtoList);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ExceptionDto> handleArgumentTypeMismatch(MethodArgumentTypeMismatchException e) {
        ApiErrorType errorType = ApiErrorType.ARGUMENT_TYPE_MISMATCH;
        List<FieldErrorDto> list = List.of(new FieldErrorDto(e.getName(),e.getMessage()));
        return exceptionResponseBuilder(errorType, list);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ExceptionDto> handleIncorrectEndpointPath(NoHandlerFoundException e) {
        ApiErrorType errorType = ApiErrorType.NO_HANDLER_FOUND;
        return exceptionResponseBuilder(errorType, null);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ExceptionDto> handleConstraintViolation(ConstraintViolationException e) {
        List<FieldErrorDto> list = e.getConstraintViolations().stream()
                .map(violation ->
                        FieldErrorDto.of(
                                violation.getMessageTemplate(),
                                violation.getMessage()))
                .toList();

        ApiErrorType errorType = ApiErrorType.CONSTRAINT_VIOLATION;

        return exceptionResponseBuilder(errorType, list);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionDto> handleRuntimeException(RuntimeException e) {
        ApiErrorType errorType = ApiErrorType.INTERNAL_SERVER_ERROR;
        return exceptionResponseBuilder(errorType, null);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionDto> handleBookNotFoundException(BookNotFoundException e) {
        ApiErrorType errorType = ApiErrorType.INTERNAL_SERVER_ERROR;
        return exceptionResponseBuilder(errorType, null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDto> handleUnknown(Exception e) {
        ApiErrorType errorType = ApiErrorType.INTERNAL_SERVER_ERROR;
        return exceptionResponseBuilder(errorType, null);
    }

    private ResponseEntity<ExceptionDto> exceptionResponseBuilder(ApiErrorType errorType, List<FieldErrorDto> list) {
        return ResponseEntity
                .status(errorType.getStatus())
                .body(
                        new ExceptionDto(
                                errorType.getStatus(),
                                errorType.getType(),
                                errorType.getMessage(),
                                list));
    }


}
