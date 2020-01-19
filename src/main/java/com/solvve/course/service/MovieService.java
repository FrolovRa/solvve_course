package com.solvve.course.service;

import com.solvve.course.domain.Movie;
import com.solvve.course.dto.MovieCreateDto;
import com.solvve.course.dto.MovieReadDto;
import com.solvve.course.exception.EntityNotFoundException;
import com.solvve.course.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    public MovieReadDto getMovie(UUID id) {
        Movie movieFromDb = movieRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Movie.class, id));
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

        movie = movieRepository.save(movie);
        return mapToReadDto(movie);
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
        return dto;
    }
}
