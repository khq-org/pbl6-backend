package com.backend.pbl6schoolsystem.common.exception;

import com.backend.pbl6schoolsystem.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.Timestamp;

@RestControllerAdvice
public class RequestExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleErrorNotFoundException(NotFoundException exception) {
        Response<?> error = Response.builder()
                .setSuccess(false)
                .setMessage(exception.getMessage())
                .setTimestamp(new Timestamp(System.currentTimeMillis()))
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(ErrorResponseRuntimeException.class)
    public ResponseEntity<?> handleErrorResponseRuntimeException(ErrorResponseRuntimeException exception) {
        Response<?> errors = Response.builder()
                .setSuccess(false)
                .setMessage(exception.getMessage())
                .setErrorDTOs(exception.getErrorDTOs())
                .setTimestamp(new Timestamp(System.currentTimeMillis()))
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

}
