package com.solvve.course.dto.character;

import com.solvve.course.dto.actor.ActorReadDto;
import com.solvve.course.dto.movie.MovieReadDto;
import com.solvve.course.dto.review.CharacterReviewReadDto;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CharacterCreateDto {

    private String name;

    private ActorReadDto actor;

    private MovieReadDto movie;

    private List<CharacterReviewReadDto> reviews = new ArrayList<>();
}