package com.solvve.course.repository;

import com.solvve.course.BaseTest;
import com.solvve.course.domain.Movie;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class MovieRepositoryTest extends BaseTest {

    @Test
    public void testSave() {
        Movie movie = utils.getMovieFromDb();
        Movie movieFromDb = movieRepository.save(movie);
        assertNotNull(movieFromDb);
        assertTrue(movieRepository.findById(movieFromDb.getId()).isPresent());
    }

    @Test
    public void testGetAllMoviesIds() {
        Set<UUID> expectedIds = new HashSet<>();
        expectedIds.add(utils.getMovieFromDb().getId());
        expectedIds.add(utils.getMovieFromDb().getId());

        utils.inTransaction(() -> {
            Assert.assertEquals(expectedIds, movieRepository.getIdsOfAllMovies().collect(Collectors.toSet()));
        });
    }
}
