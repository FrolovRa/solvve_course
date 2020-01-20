package com.solvve.course.dto;

import com.solvve.course.domain.Character;
import com.solvve.course.domain.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class ActorReadDto {

    private UUID id;

    private Person person;

    private List<Movie> movies = new ArrayList<>();

    private List<Movie> moviesAsStar = new ArrayList<>();

    private List<Character> characters = new ArrayList<>();
}
