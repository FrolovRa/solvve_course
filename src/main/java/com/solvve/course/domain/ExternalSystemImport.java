package com.solvve.course.domain;

import com.solvve.course.domain.constant.ImportedEntityType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Entity
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class ExternalSystemImport extends AbstractEntity {

    @NotNull
    @Enumerated(EnumType.STRING)
    private ImportedEntityType entityType;

    @NotNull
    private UUID entityId;

    @NotNull
    private String idInExternalSystem;
}
