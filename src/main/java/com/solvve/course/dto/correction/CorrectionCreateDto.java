package com.solvve.course.dto.correction;

import lombok.Data;

import java.util.UUID;

@Data
public class CorrectionCreateDto {

    private UUID userId;

    private UUID publicationId;

    private String selectedText;

    private String proposedText;
}
