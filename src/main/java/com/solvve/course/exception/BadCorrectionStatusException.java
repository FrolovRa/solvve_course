package com.solvve.course.exception;

import com.solvve.course.domain.constant.CorrectionStatus;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class BadCorrectionStatusException extends RuntimeException {

    public BadCorrectionStatusException(UUID id, CorrectionStatus status) {
        super(String.format("Correction with id=%s has bad %s status", id, status));
    }
}
