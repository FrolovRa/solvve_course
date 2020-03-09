package com.solvve.course.repository;

import com.solvve.course.domain.Movie;
import com.solvve.course.util.TestUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(statements = "delete from movie", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class MovieRepositoryTest {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private TestUtils utils;

    @Test
    public void testSave() {
        Movie movie = new Movie();
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
