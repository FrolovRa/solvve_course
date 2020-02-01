package com.solvve.course.dto.movie;

import com.solvve.course.domain.constant.Genre;
import com.solvve.course.dto.actor.ActorReadDto;
import com.solvve.course.dto.character.CharacterReadDto;
import lombok.Data;

import java.time.LocalDate;
import java.util.*;

@Data
public class MovieReadDto {

    private UUID id;

    private String name;

    private String description;

    private LocalDate release;

    private Set<Genre> genres = new HashSet<>();

    private List<CharacterReadDto> characters = new ArrayList<>();

    private List<ActorReadDto> cast = new ArrayList<>();

    private List<ActorReadDto> stars = new ArrayList<>();
}
