package com.solvve.course.dto.actor;

import com.solvve.course.domain.Person;
import com.solvve.course.dto.character.CharacterReadDto;
import com.solvve.course.dto.movie.MovieReadDto;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ActorPatchDto {

    private Person person;

    private List<MovieReadDto> movies = new ArrayList<>();

    private List<MovieReadDto> moviesAsStar = new ArrayList<>();

    private List<CharacterReadDto> characters = new ArrayList<>();
}
