package com.example.balanceGame.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<Object> duplicateUserException(DuplicateUserException ex) {
        return new ResponseEntity<>(Message.DUPLICATE_USER, HttpStatus.BAD_REQUEST);
    }
}