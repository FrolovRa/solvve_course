package com.solvve.course.dto.character;

import com.solvve.course.dto.actor.ActorReadDto;
import com.solvve.course.dto.movie.MovieReadDto;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class CharacterReadDto {

    private UUID id;

    private String name;

    private ActorReadDto actor;

    private MovieReadDto movie;

    private Instant createdAt;

    private Instant updatedAt;
}
