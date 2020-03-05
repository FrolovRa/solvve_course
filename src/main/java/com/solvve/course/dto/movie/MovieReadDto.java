package com.solvve.course.dto.movie;

import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class MovieReadDto {

    private UUID id;

    private String name;

    private String description;

    private LocalDate release;

    private Instant createdAt;

    private Instant updatedAt;
}
