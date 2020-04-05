package com.solvve.course.repository;

import com.solvve.course.domain.Movie;
import com.solvve.course.dto.movie.MovieFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MovieRepositoryCustom {

    Page<Movie> findByFilter(MovieFilter filter, Pageable pageable);
}
