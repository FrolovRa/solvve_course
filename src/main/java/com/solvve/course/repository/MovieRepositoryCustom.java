package com.solvve.course.repository;

import com.solvve.course.domain.Movie;
import com.solvve.course.dto.movie.MovieFilter;

import java.util.List;

public interface MovieRepositoryCustom {
    List<Movie> findByFilter(MovieFilter filter);
}
