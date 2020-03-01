package com.solvve.course.dto.actor;

import com.solvve.course.dto.character.CharacterReadDto;
import com.solvve.course.dto.movie.MovieReadDto;
import com.solvve.course.dto.person.PersonReadDto;
import lombok.Data;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class ActorExtendedReadDto {

    private UUID id;

    private PersonReadDto person;

    private Instant createdAt;

    private Instant updatedAt;

    private List<MovieReadDto> movies = new ArrayList<>();

    private List<MovieReadDto> moviesAsStar = new ArrayList<>();

    private List<CharacterReadDto> characters = new ArrayList<>();
}
