package com.example.balanceGame.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<Object> duplicateUserIdException(DuplicateUserIdException ex) {
        return new ResponseEntity<>(Message.DUPLICATE_USERID, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<Object> duplicateUserEmailException(DuplicateUserEmailException ex) {
        return new ResponseEntity<>(Message.DUPLICATE_USEREMAIL, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<Object> encryptionException(PasswordMismatchException ex) {
        return new ResponseEntity<>(Message.PASSWORD_MISMATCH, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseEntity<Object> notFoundException(NotFoundException ex) {
        return new ResponseEntity<>(Message.NOT_FOUND_USER, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseEntity<Object> failedCreateTokenException(FailedCreateTokenException ex) {
        return new ResponseEntity<>(Message.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseEntity<Object> failedRegenerateTokenException(FailedRegenerateTokenException ex) {
        return new ResponseEntity<>(Message.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseEntity<Object> failedDeleteException(FailedDeleteException ex) {
        return new ResponseEntity<>(Message.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseEntity<Object> failedFindException(FailedFindException ex) {
        return new ResponseEntity<>(Message.FAILED_FIND_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}