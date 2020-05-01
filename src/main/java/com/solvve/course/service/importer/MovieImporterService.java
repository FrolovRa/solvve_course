package com.solvve.course.service.importer;

import com.solvve.course.client.themoviedb.TheMovieDbClient;
import com.solvve.course.client.themoviedb.dto.MovieReadDto;
import com.solvve.course.domain.Movie;
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

    public UUID importMovie(String movieExternalId) {
        MovieReadDto externalMovie = theMovieDbClient.getMovie(movieExternalId);

        Movie movie = new Movie();
        movie.setName(externalMovie.getTitle());
        movieRepository.save(movie);

        return movie.getId();
    }
}
