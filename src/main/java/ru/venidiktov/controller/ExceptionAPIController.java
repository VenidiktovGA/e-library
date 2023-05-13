package ru.venidiktov.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.venidiktov.exception.BookNotFoundException;
import ru.venidiktov.util.ErrorResponse;

@RestControllerAdvice
public class ExceptionAPIController {

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> bookNotFound(BookNotFoundException bookNotFoundException) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(bookNotFoundException.getMessage(), System.currentTimeMillis()));
    }
}
