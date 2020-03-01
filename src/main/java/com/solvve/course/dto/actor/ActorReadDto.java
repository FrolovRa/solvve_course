package com.solvve.course.dto.actor;

import com.solvve.course.dto.person.PersonReadDto;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class ActorReadDto {

    private UUID id;

    private PersonReadDto person;

    private Instant createdAt;

    private Instant updatedAt;
}
