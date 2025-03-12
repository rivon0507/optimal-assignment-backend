package io.github.rivon0507.optimalassignment.backend.config;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(HttpMessageConversionException.class)
    public ResponseEntity<?> handleHttpMessageConversionException(@NotNull HttpMessageConversionException e) {
        return new ResponseEntity<>(e.getCause().getMessage(), HttpStatus.BAD_REQUEST);
    }
}
