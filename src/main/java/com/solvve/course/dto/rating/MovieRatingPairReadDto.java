package com.solvve.course.dto.rating;

import com.solvve.course.dto.movie.MovieReadDto;
import com.solvve.course.dto.user.UserReadDto;
import lombok.Data;

import java.util.UUID;

@Data
public class MovieRatingPairReadDto {

    private UUID id;

    private UserReadDto user;

    private MovieReadDto movie;

    private double value;
}
