package com.solvve.course.dto.actor;

import com.solvve.course.dto.character.CharacterReadDto;
import com.solvve.course.dto.movie.MovieReadDto;
import com.solvve.course.dto.person.PersonReadDto;
import lombok.Data;

import java.util.List;

@Data
public class ActorPatchDto {

    private PersonReadDto person;

    private List<MovieReadDto> movies;

    private List<MovieReadDto> moviesAsStar;

    private List<CharacterReadDto> characters;
}
