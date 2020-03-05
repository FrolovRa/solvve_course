package com.solvve.course.service;

import com.solvve.course.domain.Movie;
import com.solvve.course.dto.movie.MovieCreateDto;
import com.solvve.course.dto.movie.MovieFilter;
import com.solvve.course.dto.movie.MoviePatchDto;
import com.solvve.course.dto.movie.MovieReadDto;
import com.solvve.course.exception.EntityNotFoundException;
import com.solvve.course.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
        Movie movieFromDb = this.getMovieRequired(id);

        return translationService.toReadDto(movieFromDb);
    }

    public List<MovieReadDto> getMovies(MovieFilter filter) {
     List<Movie> movies = movieRepository.findByFilter(filter);
     return movies.stream()
             .map(translationService::toReadDto)
             .collect(Collectors.toList());
    }

    public MovieReadDto addMovie(MovieCreateDto movieCreateDto) {
        Movie movie = translationService.toEntity(movieCreateDto);
        movie = movieRepository.save(movie);

        return translationService.toReadDto(movie);
    }

    public MovieReadDto patchMovie(UUID id, MoviePatchDto moviePatchDto) {
        Movie movieFromDb = this.getMovieRequired(id);
        if (nonNull(moviePatchDto.getName())) {
            movieFromDb.setName(moviePatchDto.getName());
        }
        if (nonNull(moviePatchDto.getRelease())) {
            movieFromDb.setRelease(moviePatchDto.getRelease());
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
