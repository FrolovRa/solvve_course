package com.solvve.course.repository;

import com.solvve.course.domain.Movie;
import com.solvve.course.domain.constant.Genre;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface MovieRepository extends CrudRepository<Movie, UUID> {
    List<Movie> findMoviesByGenres(Genre genre);

    @Query(value = "SELECT m FROM Movie m WHERE m.release >= :from AND m.release < :to")
    List<Movie> findMovieByReleaseInInterval(LocalDate from, LocalDate to);
}
