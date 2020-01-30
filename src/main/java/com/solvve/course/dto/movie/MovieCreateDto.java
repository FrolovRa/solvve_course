package com.solvve.course.dto.movie;

import com.solvve.course.domain.constant.Genre;
import com.solvve.course.dto.actor.ActorReadDto;
import com.solvve.course.dto.character.CharacterReadDto;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class MovieCreateDto {

    private String name;

    private String description;

    private Set<Genre> genres = new HashSet<>();

    private List<CharacterReadDto> characters = new ArrayList<>();

    private List<ActorReadDto> cast = new ArrayList<>();

    private List<ActorReadDto> stars = new ArrayList<>();
}
