package com.solvve.course.dto.correction;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class CorrectionCreateDto {

    @NotNull
    private UUID userId;

    @NotNull
    private UUID publicationId;

    @NotNull
    private Integer startIndex;

    @NotNull
    private String selectedText;

    private String proposedText;
}
