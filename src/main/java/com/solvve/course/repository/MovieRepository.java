package com.solvve.course.repository;

import com.solvve.course.domain.Movie;
import com.solvve.course.domain.constant.Genre;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MovieRepository extends CrudRepository<Movie, UUID> {
    List<Movie> findMoviesByGenres(Genre genre);
}
