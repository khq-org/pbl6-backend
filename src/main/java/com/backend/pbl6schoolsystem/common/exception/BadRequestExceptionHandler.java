package com.backend.pbl6schoolsystem.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.Timestamp;

@ControllerAdvice
public class BadRequestExceptionHandler {
    @ExceptionHandler(value = BadRequestException.class)
    public ResponseEntity<Object> handler(BadRequestException badRequestException) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ApiException apiException = new ApiException(
                badRequestException.getMessage(),
                status,
                new Timestamp(System.currentTimeMillis())
        );
        return new ResponseEntity<>(apiException, status);
    }
}
