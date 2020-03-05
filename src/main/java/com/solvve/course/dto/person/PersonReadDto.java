package com.solvve.course.dto.person;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class PersonReadDto {

    private UUID id;

    private String name;

    private Instant createdAt;

    private Instant updatedAt;
}
