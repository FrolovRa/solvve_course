package com.solvve.course.client.themoviedb;

import com.solvve.course.BaseTest;
import com.solvve.course.client.themoviedb.dto.MovieReadDto;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

public class TheMovieDbClientTest extends BaseTest {

    @Autowired
    private TheMovieDbClient theMovieDbClient;

    @Test
    public void testGetMovie() {
        final String movieId = "280";

        MovieReadDto movie = theMovieDbClient.getMovie(movieId);

        System.out.println(movie);
        assertEquals(movieId, movie.getId());
        assertEquals("Terminator 2: Judgment Day", movie.getOriginalTitle());
        assertEquals(movie.getOriginalTitle(), movie.getTitle());
    }
}