package com.solvve.course.exception;

import com.solvve.course.domain.ExternalSystemImport;
import lombok.Getter;

@Getter
public class ImportAlreadyPerformedException extends Exception {

    private final ExternalSystemImport externalSystemImport;

    public ImportAlreadyPerformedException(ExternalSystemImport esi) {
        super(String.format(
            "Already performed import of %s with id=%s and id in external system = %s",
            esi.getEntityType(),
            esi.getEntityId(),
            esi.getIdInExternalSystem())
        );
        this.externalSystemImport = esi;
    }
}