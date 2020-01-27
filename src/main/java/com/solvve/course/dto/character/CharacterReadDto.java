package com.solvve.course.dto.character;

import com.solvve.course.dto.actor.ActorReadDto;
import com.solvve.course.dto.movie.MovieReadDto;
import com.solvve.course.dto.review.CharacterReviewReadDto;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class CharacterReadDto {

    private UUID id;

    private String name;

    private ActorReadDto actor;

    private double rating;

    private MovieReadDto movie;

    private List<CharacterReviewReadDto> reviews = new ArrayList<>();
}
