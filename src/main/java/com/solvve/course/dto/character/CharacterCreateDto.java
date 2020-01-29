package com.solvve.course.dto.character;

import com.solvve.course.dto.actor.ActorReadDto;
import com.solvve.course.dto.movie.MovieReadDto;
import lombok.Data;

@Data
public class CharacterCreateDto {

    private String name;

    private ActorReadDto actor;

    private double rating;

    private MovieReadDto movie;

}
