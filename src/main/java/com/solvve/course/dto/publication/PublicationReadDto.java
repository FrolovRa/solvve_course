package com.solvve.course.dto.publication;

import com.solvve.course.dto.principal.PrincipalReadDto;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class PublicationReadDto {

    private UUID id;

    private Instant createdAt;

    private Instant updatedAt;

    private PrincipalReadDto manager;

    private String title;

    private String content;
}
