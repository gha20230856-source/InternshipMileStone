package com.example.InternshipMileStone.exceptionhandler;


import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Returns HTTP 403 Forbidden
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDenied(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }

    // Returns HTTP 404 Not Found
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleNotFound(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> constraintViolation(DataIntegrityViolationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("bad input to database " + ex.getMessage());
    }

    @ExceptionHandler(HttpServerErrorException.InternalServerError.class)
    public ResponseEntity<String> unknownError(HttpServerErrorException.InternalServerError ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("An Error Occured");
    }

    @ExceptionHandler(HttpClientErrorException.Forbidden.class)
    public ResponseEntity<String> accessDenied(HttpClientErrorException.Forbidden ex) {
        return  ResponseEntity.status(HttpStatus.FORBIDDEN).body("you can't access this resource");
    }
}
