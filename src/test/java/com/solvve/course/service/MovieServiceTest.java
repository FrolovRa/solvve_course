package com.solvve.course.service;

import com.solvve.course.domain.Movie;
import com.solvve.course.domain.constant.Genre;
import com.solvve.course.dto.MovieCreateDto;
import com.solvve.course.dto.MovieReadDto;
import com.solvve.course.exception.EntityNotFoundException;
import com.solvve.course.repository.MovieRepository;
import org.hibernate.Hibernate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;

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
    @Transactional
    public void testGetMovie() {
        Movie movie = new Movie();
        movie.setName("Shattered island");
        movie.setDescription("cool film");
        movie.setGenres(new HashSet<>(Arrays.asList(Genre.DRAMA, Genre.ADVENTURE)));

        movie = movieRepository.save(movie);

        MovieReadDto movieReadDto = movieService.getMovie(movie.getId());
        assertThat(movieReadDto).isEqualToComparingFieldByField(movie);
    }

//    @Test
//    public void testCreateMovie() {
//        MovieCreateDto movieCreateDto = new MovieCreateDto();
//        movieCreateDto.setName("Shattered island");
//        movieCreateDto.setGenre("Drama");
//        movieCreateDto.setMainActor("Leonardo DiCaprio");
//
//        MovieReadDto movieReadDto = movieService.addMovie(movieCreateDto);
//
//        assertThat(movieCreateDto).isEqualToComparingFieldByField(movieReadDto);
//        assertNotNull(movieReadDto.getId());
//
//        Movie movieFromDb = movieRepository.findById(movieReadDto.getId()).get();
//        assertThat(movieReadDto).isEqualToComparingFieldByField(movieFromDb);
//    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetMovieByWrongId() {
        movieService.getMovie(UUID.randomUUID());
    }
}
