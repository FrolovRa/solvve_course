package com.solvve.course.dto;

import com.solvve.course.domain.*;
import com.solvve.course.domain.Character;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
public class MoviePatchDto {

    private String name;

    private String description;

    private Set genres;

    private List<Character> characters;

    private List<Actor> cast;

    private List<Actor> stars;

    private List<Director> directors;

    private List<Writer> writers;

    private List<MovieReview> reviews;
}
