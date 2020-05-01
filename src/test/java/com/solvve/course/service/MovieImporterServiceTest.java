package com.solvve.course.service;

import com.solvve.course.BaseTest;
import com.solvve.course.client.themoviedb.TheMovieDbClient;
import com.solvve.course.client.themoviedb.dto.MovieReadDto;
import com.solvve.course.domain.Movie;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class MovieImporterServiceTest extends BaseTest {

    @MockBean
    private TheMovieDbClient theMovieDbClient;

    @Test
    public void testImportMovie() {
        final String movieExternalId = "100";
        MovieReadDto externalMovie = new MovieReadDto();
        externalMovie.setOriginalTitle("I Origin");

        when(theMovieDbClient.getMovie(movieExternalId)).thenReturn(externalMovie);

        UUID savedMovieId = movieImporterService.importMovie(movieExternalId);
        Movie savedMovie = movieRepository.findById(savedMovieId).get();

        assertEquals(externalMovie.getOriginalTitle(), savedMovie.getName());
    }
}
