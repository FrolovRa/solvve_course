package com.solvve.course.repository;

import com.solvve.course.domain.Movie;
import com.solvve.course.service.TranslationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(statements = "delete from movie", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class MovieRepositoryTest {

    @Autowired
    private MovieRepository movieRepository;

    @Test
    public void testSave() {
        Movie movie = new Movie();
        Movie movieFromDb = movieRepository.save(movie);
        assertNotNull(movieFromDb);
        assertTrue(movieRepository.findById(movieFromDb.getId()).isPresent());
    }
}
