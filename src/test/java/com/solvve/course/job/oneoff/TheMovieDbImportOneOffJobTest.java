package com.solvve.course.job.oneoff;

import com.solvve.course.BaseTest;
import com.solvve.course.client.themoviedb.TheMovieDbClient;
import com.solvve.course.client.themoviedb.dto.MovieReadShortDto;
import com.solvve.course.client.themoviedb.dto.MoviesPageDto;
import com.solvve.course.exception.ImportAlreadyPerformedException;
import com.solvve.course.exception.ImportedEntityAlreadyExistException;
import com.solvve.course.service.importer.MovieImporterService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.mockito.Mockito.*;

public class TheMovieDbImportOneOffJobTest extends BaseTest {

    @MockBean
    private TheMovieDbClient theMovieDbClient;

    @MockBean
    private MovieImporterService movieImporterService;

    @Autowired
    private TheMovieDbImportOneOffJob theMovieDbImportOneOffJob;

    @Test
    public void testDoImport() throws ImportedEntityAlreadyExistException, ImportAlreadyPerformedException {
        MoviesPageDto response = generatePageWith2Movies();
        when(theMovieDbClient.getTopRatedMovies()).thenReturn(response);

        theMovieDbImportOneOffJob.doImport();

        for (MovieReadShortDto m : response.getResults()) {
            verify(movieImporterService).importMovie(m.getId());
        }
    }

    @Test
    public void testDoImportFirstFailedSecondSuccess()
        throws ImportedEntityAlreadyExistException, ImportAlreadyPerformedException {

        MoviesPageDto response = generatePageWith2Movies();
        when(theMovieDbClient.getTopRatedMovies()).thenReturn(response);
        when(movieImporterService.importMovie(response.getResults().get(0).getId()))
            .thenThrow(ImportedEntityAlreadyExistException.class);

        theMovieDbImportOneOffJob.doImport();

        for (MovieReadShortDto m : response.getResults()) {
            verify(movieImporterService).importMovie(m.getId());
        }
    }

    @Test
    public void testDoImportNoExceptionsIfGetTopFailed() {
        when(theMovieDbClient.getTopRatedMovies()).thenThrow(RuntimeException.class);

        theMovieDbImportOneOffJob.doImport();

        verifyNoInteractions(movieImporterService);
    }

    private MoviesPageDto generatePageWith2Movies() {
        MoviesPageDto page = new MoviesPageDto();
        MovieReadShortDto firstMovie = new MovieReadShortDto();
        firstMovie.setId("101");
        firstMovie.setTitle("Wall-e");
        MovieReadShortDto secondMovie = new MovieReadShortDto();
        secondMovie.setId("102");
        secondMovie.setTitle("Nine");
        page.setResults(List.of(firstMovie, secondMovie));

        return page;
    }
}