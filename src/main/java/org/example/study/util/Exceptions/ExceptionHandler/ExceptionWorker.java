package org.example.study.util.Exceptions.ExceptionHandler;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.example.study.util.Exceptions.CustomExceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.List;
import java.util.stream.Collectors;


@ControllerAdvice
public class ExceptionWorker {

    //Just a generic attempt at working with exceptions
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionDto> handleRuntimeException(RuntimeException e) {
        return ResponseEntity.internalServerError().body(new ExceptionDto(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", "Something went wrong on the server side", null));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionDto> handleBodyException(MethodArgumentNotValidException e) {
        List<FieldErrorDto> message = e.getBindingResult().getFieldErrors().stream()
                .map(err -> new FieldErrorDto(err.getField(), err.getDefaultMessage())).toList();
        return ResponseEntity.badRequest().body(new ExceptionDto(HttpStatus.BAD_REQUEST, "Validation error", "Incorrect method arguments provided", message));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionDto> handleGetNoUsersException(UserNotFoundException e) {
        ExceptionDto dto = new ExceptionDto(HttpStatus.NOT_FOUND, e.getMessage());
        return new ResponseEntity<>(dto, dto.statusCode());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ExceptionDto> handleArgumentTypeMismatch(MethodArgumentTypeMismatchException e) {
        ExceptionDto dto = new ExceptionDto(HttpStatus.BAD_REQUEST, "incorrect argument for -> " + e.getParameter().getParameterName());
        return new ResponseEntity<>(dto, dto.statusCode());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ExceptionDto> handleIncorrectEndpointPath(NoHandlerFoundException e) {
        ExceptionDto dto = new ExceptionDto(HttpStatus.NOT_FOUND, "haven't found a method in controller to handle this request. -> " + e.getMessage());
        return new ResponseEntity<>(dto, dto.statusCode());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ExceptionDto> handleConstraintViolation(ConstraintViolationException e) {
        ExceptionDto dto = new ExceptionDto(HttpStatus.BAD_REQUEST, e.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.joining("\n , ")));
        return new ResponseEntity<>(dto, dto.statusCode());
    }


}
