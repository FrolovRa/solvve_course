package com.solvve.course.service;

import com.solvve.course.domain.Movie;
import com.solvve.course.dto.MovieReadDto;
import com.solvve.course.repository.MovieRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(statements = "delete from movie", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class MovieServiceTest {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private MovieService movieService;

    @Test
    public void testGetMovie() {
        Movie movie = new Movie();
        movie.setName("Shattered island");
        movie.setGenre("Drama");
        movie.setMainActor("Leonardo DiCaprio");
        movie.setRating(8.1d);

        movie = movieRepository.save(movie);

        MovieReadDto movieReadDto = movieService.getMovie(movie.getId());

        assertThat(movieReadDto).isEqualToComparingFieldByField(movie);
    }
}
