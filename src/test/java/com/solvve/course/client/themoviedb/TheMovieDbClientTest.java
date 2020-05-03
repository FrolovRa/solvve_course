package com.solvve.course.client.themoviedb;

import com.solvve.course.BaseTest;
import com.solvve.course.client.themoviedb.dto.MovieReadDto;
import com.solvve.course.client.themoviedb.dto.MoviesPageDto;
import com.solvve.course.domain.constant.Genre;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.Set;

import static com.solvve.course.domain.constant.Genre.*;
import static org.junit.Assert.*;

public class TheMovieDbClientTest extends BaseTest {

    @Autowired
    private TheMovieDbClient theMovieDbClient;

    @Test
    public void testGetMovie() {
        final String movieId = "280";
        final Set<Genre> genres = Set.of(ACTION, THRILLER, SCIENCE_FICTION);
        final LocalDate releaseDate = LocalDate.of(1991, 7, 3);

        MovieReadDto movie = theMovieDbClient.getMovie(movieId, "en");

        assertEquals(movieId, movie.getId());
        assertEquals("Terminator 2: Judgment Day", movie.getTitle());
        assertEquals(genres, movie.getGenres());
        assertEquals(releaseDate, movie.getReleaseDate());
    }

    @Test
    public void testGetTopRatingMovies() {
        MoviesPageDto result = theMovieDbClient.getTopRatedMovies();
        assertTrue(result.getTotalPages() > 0);
        assertTrue(result.getTotalResults() > 0);
        assertTrue(result.getResults().size() > 0);
        result.getResults().forEach(movie -> {
            assertNotNull(movie.getTitle());
            assertNotNull(movie.getId());
        });
    }
}