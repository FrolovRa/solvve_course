package com.solvve.course.dto;

import com.solvve.course.domain.Character;
import com.solvve.course.domain.Movie;
import com.solvve.course.domain.Person;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ActorCreateDto {

    private Person person;

    private List<Movie> movies = new ArrayList<>();

    private List<Movie> moviesAsStar = new ArrayList<>();

    private List<Character> characters = new ArrayList<>();
}
