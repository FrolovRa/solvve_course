package com.solvve.course.repository;

import com.solvve.course.domain.Movie;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.stream.Stream;

@Repository
public interface MovieRepository extends CrudRepository<Movie, UUID>, MovieRepositoryCustom {

    @Query(value = "SELECT AVG(m.rating) FROM Rating m WHERE m.entityId = :movieId")
    Double calcAverageRating(UUID movieId);

    @Query(value = "SELECT m.id FROM Movie m")
    Stream<UUID> getIdsOfAllMovies();
}