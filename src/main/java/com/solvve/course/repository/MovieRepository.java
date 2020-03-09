package com.solvve.course.repository;

import com.solvve.course.domain.Movie;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface MovieRepository extends CrudRepository<Movie, UUID>, MovieRepositoryCustom {

    @Query(value = "SELECT m FROM Movie m WHERE m.release >= :from AND m.release < :to")
    List<Movie> findMovieByReleaseInInterval(LocalDate from, LocalDate to);

    @Query(value = "SELECT AVG(m.rating) FROM Rating m WHERE m.entityId = :movieId")
    Double calcAverageRating(UUID movieId);
}