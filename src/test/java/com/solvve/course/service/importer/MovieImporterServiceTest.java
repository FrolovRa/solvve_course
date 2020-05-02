package com.solvve.course.service.importer;

import com.solvve.course.BaseTest;
import com.solvve.course.client.themoviedb.TheMovieDbClient;
import com.solvve.course.client.themoviedb.dto.MovieReadDto;
import com.solvve.course.domain.Movie;
import com.solvve.course.domain.constant.Genre;
import com.solvve.course.exception.ImportAlreadyPerformedException;
import com.solvve.course.exception.ImportedEntityAlreadyExistException;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class MovieImporterServiceTest extends BaseTest {

    @MockBean
    private TheMovieDbClient theMovieDbClient;

    @Test
    public void testImportMovie() throws ImportedEntityAlreadyExistException, ImportAlreadyPerformedException {
        final String movieExternalId = "100";
        MovieReadDto externalMovie = new MovieReadDto();
        externalMovie.setTitle("I Origin");
        externalMovie.setGenres(Set.of(Genre.DRAMA));
        externalMovie.setOverview("drama movie");
        externalMovie.setReleaseDate(LocalDate.of(2018, 5, 10));

        when(theMovieDbClient.getMovie(movieExternalId, "en")).thenReturn(externalMovie);

        UUID savedMovieId = movieImporterService.importMovie(movieExternalId);
        Movie savedMovie = movieRepository.findById(savedMovieId).get();

        assertEquals(externalMovie.getTitle(), savedMovie.getName());
    }

    @Test
    public void testImportAlreadyExistMovie() {
        final String movieExternalId = "100";
        Movie existingMovie = utils.getMovieFromDb();
        MovieReadDto externalMovie = new MovieReadDto();
        externalMovie.setTitle(existingMovie.getName());
        externalMovie.setReleaseDate(existingMovie.getReleaseDate());

        when(theMovieDbClient.getMovie(movieExternalId, "en")).thenReturn(externalMovie);

        ImportedEntityAlreadyExistException exception = Assertions.catchThrowableOfType(
            () -> movieImporterService.importMovie(movieExternalId),
            ImportedEntityAlreadyExistException.class
        );
        assertEquals(Movie.class, exception.getEntityClass());
        assertEquals(existingMovie.getId(), exception.getEntityId());
    }

    @Test
    public void testNoCallToExternalSystemOnDuplicateImport()
        throws ImportedEntityAlreadyExistException, ImportAlreadyPerformedException {

        final String movieExternalId = "100";

        MovieReadDto externalMovie = new MovieReadDto();
        externalMovie.setTitle("I Origin");
        externalMovie.setGenres(Set.of(Genre.DRAMA));
        externalMovie.setOverview("drama movie");
        externalMovie.setReleaseDate(LocalDate.of(2018, 5, 10));
        when(theMovieDbClient.getMovie(movieExternalId, "en")).thenReturn(externalMovie);

        movieImporterService.importMovie(movieExternalId);
        verify(theMovieDbClient).getMovie(movieExternalId, "en");
        reset(theMovieDbClient);

        Assertions.assertThatThrownBy(() -> movieImporterService.importMovie(movieExternalId))
            .isInstanceOf(ImportAlreadyPerformedException.class);
        verifyNoInteractions(theMovieDbClient);
    }
}