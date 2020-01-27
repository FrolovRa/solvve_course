package com.solvve.course.dto.rating;

import com.solvve.course.dto.movie.MovieReadDto;
import com.solvve.course.dto.user.UserReadDto;
import lombok.Data;

@Data
public class MovieRatingPairPatchDto {

    private UserReadDto user;

    private MovieReadDto movie;

    private double value;
}
