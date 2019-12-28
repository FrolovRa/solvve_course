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
    MovieRepository movieRepository;

    public MovieReadDto getMovie(UUID id) {
        Movie movieFromDb = movieRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Movie.class, id));
        return mapToReadDto(movieFromDb);
    }

    public MovieReadDto addMovie(MovieCreateDto movieCreateDto) {
        Movie movie = new Movie();
        movie.setName(movieCreateDto.getName());
        movie.setGenre(movieCreateDto.getGenre());
        movie.setMainActor(movieCreateDto.getMainActor());

        movie = movieRepository.save(movie);
        return mapToReadDto(movie);
    }

    private MovieReadDto mapToReadDto(Movie movie) {
        MovieReadDto dto = new MovieReadDto();
        dto.setId(movie.getId());
        dto.setRating(movie.getRating());
        dto.setName(movie.getName());
        dto.setGenre(movie.getGenre());
        dto.setMainActor(movie.getMainActor());
        return dto;
    }

}
