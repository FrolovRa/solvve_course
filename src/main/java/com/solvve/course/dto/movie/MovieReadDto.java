package com.solvve.course.dto.movie;

import com.solvve.course.domain.constant.Genre;
import com.solvve.course.dto.actor.ActorReadDto;
import com.solvve.course.dto.character.CharacterReadDto;
import lombok.Data;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
public class MovieReadDto {

    private UUID id;

    private String name;

    private String description;

    private Set<Genre> genres;

    private List<CharacterReadDto> characters;

    private List<ActorReadDto> cast;

    private List<ActorReadDto> stars;
}
