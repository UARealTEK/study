package org.example.study.util.Exceptions.ExceptionHandler;

import jakarta.validation.ConstraintViolationException;
import org.example.study.util.Exceptions.CustomExceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unused")
@RestControllerAdvice
public class ExceptionWorker {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionDto> handleBodyException(MethodArgumentNotValidException e) {
        List<FieldErrorDto> message = e.getBindingResult().getFieldErrors().stream()
                .map(err -> new FieldErrorDto(err.getField(), err.getDefaultMessage())).toList();
        return ResponseEntity.badRequest().body(new ExceptionDto(HttpStatus.BAD_REQUEST, "Validation error", "Incorrect method arguments provided", message));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionDto> handleGetNoUsersException(UserNotFoundException e) {
        ExceptionDto dto = new ExceptionDto(HttpStatus.NOT_FOUND, "Not found", "User was NOT found", List.of(new FieldErrorDto(Arrays.stream(e.getStackTrace()).toList().toString(), e.getMessage())));
        return new ResponseEntity<>(dto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ExceptionDto> handleArgumentTypeMismatch(MethodArgumentTypeMismatchException e) {
        List<FieldErrorDto> list = List.of(new FieldErrorDto(e.getName(),e.getMessage()));
        ExceptionDto dto = new ExceptionDto(HttpStatus.BAD_REQUEST, "Argument mismatch", "Incorrect body arguments", list);
        return new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ExceptionDto> handleIncorrectEndpointPath(NoHandlerFoundException e) {
        ExceptionDto dto = new ExceptionDto(HttpStatus.NOT_FOUND, "No handler found","haven't found a method in controller to handle this request. -> " + e.getMessage(), null);
        return new ResponseEntity<>(dto, dto.statusCode());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ExceptionDto> handleConstraintViolation(ConstraintViolationException e) {
        List<FieldErrorDto> list = e.getConstraintViolations().stream().map(violation -> FieldErrorDto.of(violation.getMessageTemplate(),violation.getMessage())).toList();
        ExceptionDto dto = new ExceptionDto(HttpStatus.BAD_REQUEST, "Constraint violation", null, list);
        return new ResponseEntity<>(dto, dto.statusCode());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionDto> handleRuntimeException(RuntimeException e) {
        return ResponseEntity.internalServerError().body(new ExceptionDto(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", "Something went wrong on the server side", null));
    }


}
