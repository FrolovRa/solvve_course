package com.solvve.course.service;

import com.solvve.course.domain.Movie;
import com.solvve.course.dto.movie.MovieCreateDto;
import com.solvve.course.dto.movie.MoviePatchDto;
import com.solvve.course.dto.movie.MovieReadDto;
import com.solvve.course.exception.EntityNotFoundException;
import com.solvve.course.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static java.util.Objects.nonNull;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    public MovieReadDto getMovie(UUID id) {
        Movie movieFromDb = getMovieRequired(id);
        return mapToReadDto(movieFromDb);
    }

    public MovieReadDto addMovie(MovieCreateDto movieCreateDto) {
        Movie movie = new Movie();
        movie.setName(movieCreateDto.getName());
        movie.setDescription(movieCreateDto.getDescription());
        movie.setGenres(movieCreateDto.getGenres());
        movie.setCast(movieCreateDto.getCast());
        movie.setCharacters(movieCreateDto.getCharacters());
        movie.setStars(movieCreateDto.getStars());
        movie.setDirectors(movieCreateDto.getDirectors());
        movie.setWriters(movieCreateDto.getWriters());
        movie.setPosts(null);
        movie.setReviews(null);

        movie = movieRepository.save(movie);
        return mapToReadDto(movie);
    }


    public MovieReadDto patchMovie(UUID id, MoviePatchDto moviePatchDto) {
        Movie movieFromDb = getMovieRequired(id);

        if (nonNull(moviePatchDto.getName())) {
            movieFromDb.setName(moviePatchDto.getName());
        }

        if (nonNull(moviePatchDto.getDirectors())) {
            movieFromDb.setDirectors(moviePatchDto.getDirectors());
        }

        if (nonNull(moviePatchDto.getCast())) {
            movieFromDb.setCast(moviePatchDto.getCast());
        }

        if (nonNull(moviePatchDto.getCharacters())) {
            movieFromDb.setCharacters(moviePatchDto.getCharacters());
        }

        if (nonNull(moviePatchDto.getGenres())) {
            movieFromDb.setGenres(moviePatchDto.getGenres());
        }

        if (nonNull(moviePatchDto.getStars())) {
            movieFromDb.setStars(moviePatchDto.getStars());
        }

        if (nonNull(moviePatchDto.getWriters())) {
            movieFromDb.setWriters(moviePatchDto.getWriters());
        }

        if (nonNull(moviePatchDto.getDescription())) {
            movieFromDb.setDescription(moviePatchDto.getDescription());
        }

        if (nonNull(moviePatchDto.getWriters())) {
            movieFromDb.setWriters(moviePatchDto.getWriters());
        }

        if (nonNull(moviePatchDto.getReviews())) {
            movieFromDb.setReviews(moviePatchDto.getReviews());
        }

        if (nonNull(moviePatchDto.getPosts())) {
            movieFromDb.setPosts(moviePatchDto.getPosts());
        }

        Movie patchedMovie = movieRepository.save(movieFromDb);

        return mapToReadDto(patchedMovie);
    }

    public void deleteMovie(UUID id) {
        movieRepository.delete(getMovieRequired(id));
    }

    private Movie getMovieRequired(UUID id) {
        return movieRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Movie.class, id));
    }

    private MovieReadDto mapToReadDto(Movie movie) {
        MovieReadDto dto = new MovieReadDto();
        dto.setId(movie.getId());
        dto.setName(movie.getName());
        dto.setDescription(movie.getDescription());
        dto.setGenres(movie.getGenres());
        dto.setCast(movie.getCast());
        dto.setCharacters(movie.getCharacters());
        dto.setStars(movie.getStars());
        dto.setDirectors(movie.getDirectors());
        dto.setWriters(movie.getWriters());
        dto.setReviews(movie.getReviews());
        dto.setPosts(movie.getPosts());
        return dto;
    }
}
