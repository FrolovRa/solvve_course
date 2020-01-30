package com.solvve.course.service;

import com.solvve.course.domain.Movie;
import com.solvve.course.dto.movie.MovieCreateDto;
import com.solvve.course.dto.movie.MoviePatchDto;
import com.solvve.course.dto.movie.MovieReadDto;
import com.solvve.course.exception.EntityNotFoundException;
import com.solvve.course.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private TranslationService translationService;

    public MovieReadDto getMovie(UUID id) {
        Movie movieFromDb = getMovieRequired(id);
        return translationService.toReadDto(movieFromDb);
    }

    public MovieReadDto addMovie(MovieCreateDto movieCreateDto) {
        Movie movie = translationService.toEntity(movieCreateDto);
        movie = movieRepository.save(movie);
        return translationService.toReadDto(movie);
    }


    public MovieReadDto patchMovie(UUID id, MoviePatchDto moviePatchDto) {
        Movie movieFromDb = getMovieRequired(id);

        if (nonNull(moviePatchDto.getName())) {
            movieFromDb.setName(moviePatchDto.getName());
        }

        if (nonNull(moviePatchDto.getCast())) {
            movieFromDb.setCast(moviePatchDto.getCast()
                    .stream()
                    .map(translationService::toEntity)
                    .collect(Collectors.toList()));
        }

        if (nonNull(moviePatchDto.getCharacters())) {
            movieFromDb.setCharacters(moviePatchDto.getCharacters()
                    .stream()
                    .map(translationService::toEntity)
                    .collect(Collectors.toList()));
        }

        if (nonNull(moviePatchDto.getGenres())) {
            movieFromDb.setGenres(moviePatchDto.getGenres());
        }

        if (nonNull(moviePatchDto.getStars())) {
            movieFromDb.setStars(moviePatchDto.getStars()
                    .stream()
                    .map(translationService::toEntity)
                    .collect(Collectors.toList()));
        }

        if (nonNull(moviePatchDto.getDescription())) {
            movieFromDb.setDescription(moviePatchDto.getDescription());
        }

        Movie patchedMovie = movieRepository.save(movieFromDb);

        return translationService.toReadDto(patchedMovie);
    }

    public void deleteMovie(UUID id) {
        movieRepository.delete(getMovieRequired(id));
    }

    private Movie getMovieRequired(UUID id) {
        return movieRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Movie.class, id));
    }
}
