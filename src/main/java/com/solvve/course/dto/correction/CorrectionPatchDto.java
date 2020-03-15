package com.solvve.course.dto.correction;

import com.solvve.course.domain.constant.CorrectionStatus;
import lombok.Data;

import java.util.UUID;

@Data
public class CorrectionPatchDto {

    private UUID userId;

    private UUID publicationId;

    private Integer startIndex;

    private String selectedText;

    private String proposedText;

    private CorrectionStatus status;
}