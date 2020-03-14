package com.solvve.course.dto.publication;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class PublicationPatchDto {

    private Instant createdAt;

    private Instant updatedAt;

    private UUID managerId;

    private String title;

    private String content;
}
