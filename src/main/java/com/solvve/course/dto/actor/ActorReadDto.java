package com.solvve.course.dto.actor;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class ActorReadDto {

    private UUID id;

    private UUID personId;

    private Instant createdAt;

    private Instant updatedAt;
}
