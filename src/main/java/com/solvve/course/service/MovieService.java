package com.solvve.course.service;

import com.solvve.course.domain.Movie;
import com.solvve.course.dto.MovieReadDto;
import com.solvve.course.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MovieService {

    @Autowired
    MovieRepository movieRepository;

    public MovieReadDto getMovie(UUID id) {
        Movie movieFromDb = movieRepository.findById(id).get();
        return mapToReadDto(movieFromDb);
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
