package com.solvve.course.dto.correction;

import com.solvve.course.domain.constant.CorrectionStatus;
import com.solvve.course.dto.publication.PublicationReadDto;
import com.solvve.course.dto.user.UserReadDto;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class CorrectionReadDto {

    private UUID id;

    private Instant createdAt;

    private Instant updatedAt;

    private UserReadDto user;

    private PublicationReadDto publication;

    private String selectedText;

    private String proposedText;

    private CorrectionStatus status;
}
