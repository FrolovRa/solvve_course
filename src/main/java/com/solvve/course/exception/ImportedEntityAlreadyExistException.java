package com.solvve.course.exception;

import com.solvve.course.domain.AbstractEntity;
import lombok.Getter;

import java.util.UUID;

@Getter
public class ImportedEntityAlreadyExistException extends Exception {

    private final Class<? extends AbstractEntity> entityClass;
    private final UUID entityId;

    public ImportedEntityAlreadyExistException(Class<? extends AbstractEntity> entityClass, UUID entityId, String message) {
        super(message);
        this.entityClass = entityClass;
        this.entityId = entityId;
    }
}