package com.solvve.course.exception.handler;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ErrorInfo {

    private final HttpStatus status;
    private final Class exceptionClass;
    private final String message;

    ErrorInfo(HttpStatus status, Class exceptionClass, String message) {
        this.status = status;
        this.exceptionClass = exceptionClass;
        this.message = message;
    }
}