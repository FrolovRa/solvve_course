package com.solvve.course.service.importer;

import com.solvve.course.client.themoviedb.TheMovieDbClient;
import com.solvve.course.client.themoviedb.dto.MovieReadDto;
import com.solvve.course.domain.Movie;
import com.solvve.course.exception.ImportAlreadyPerformedException;
import com.solvve.course.exception.ImportedEntityAlreadyExistException;
import com.solvve.course.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MovieImporterService {

    @Autowired
    private TheMovieDbClient theMovieDbClient;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ExternalSystemImportService externalSystemImportService;

    public UUID importMovie(String movieExternalId)
            throws ImportedEntityAlreadyExistException, ImportAlreadyPerformedException {
        externalSystemImportService.validateNotImported(Movie.class, movieExternalId);

        MovieReadDto externalMovie = theMovieDbClient.getMovie(movieExternalId, "en");

        final UUID existingMovieId = movieRepository.findMovieIdByNameAndReleaseDate(
                externalMovie.getTitle(),
                externalMovie.getReleaseDate()
        );

        if (existingMovieId != null) {
            throw new ImportedEntityAlreadyExistException(Movie.class, existingMovieId,
                    "Movie " + externalMovie.getTitle() + " already exist");
        }

        Movie movie = new Movie();
        movie.setName(externalMovie.getTitle());
        movie.setReleaseDate(externalMovie.getReleaseDate());
        movie.setGenres(externalMovie.getGenres());
        movie.setDescription(externalMovie.getOverview());
        movie = movieRepository.save(movie);

        externalSystemImportService.createExternalSystemImport(movie, movieExternalId);

        return movie.getId();
    }
}