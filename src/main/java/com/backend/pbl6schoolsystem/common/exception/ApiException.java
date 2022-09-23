package com.backend.pbl6schoolsystem.common.exception;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.sql.Timestamp;

@RequiredArgsConstructor
public class ApiException {
    private final String message;
    private final HttpStatus status;
    private final Timestamp timestamp;
}
