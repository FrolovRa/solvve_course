package com.solvve.course.service;

import com.solvve.course.domain.Movie;
import com.solvve.course.dto.PageResult;
import com.solvve.course.dto.movie.MovieCreateDto;
import com.solvve.course.dto.movie.MovieFilter;
import com.solvve.course.dto.movie.MoviePatchDto;
import com.solvve.course.dto.movie.MovieReadDto;
import com.solvve.course.repository.MovieRepository;
import com.solvve.course.repository.RepositoryHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private TranslationService translationService;

    @Autowired
    private RepositoryHelper repositoryHelper;

    public MovieReadDto getMovie(UUID id) {
        Movie movieFromDb = repositoryHelper.getEntityRequired(Movie.class, id);

        return translationService.translate(movieFromDb, MovieReadDto.class);
    }

    public PageResult<MovieReadDto> getMovies(MovieFilter filter, Pageable pageable) {
        Page<Movie> movies = movieRepository.findByFilter(filter, pageable);

        return translationService.toPageResult(movies, MovieReadDto.class);
    }

    public MovieReadDto addMovie(MovieCreateDto movieCreateDto) {
        Movie movie = translationService.translate(movieCreateDto, Movie.class);
        movie = movieRepository.save(movie);

        return translationService.translate(movie, MovieReadDto.class);
    }

    public MovieReadDto patchMovie(UUID id, MoviePatchDto moviePatchDto) {
        Movie movieFromDb = repositoryHelper.getEntityRequired(Movie.class, id);

        translationService.map(moviePatchDto, movieFromDb);
        Movie patchedMovie = movieRepository.save(movieFromDb);

        return translationService.translate(patchedMovie, MovieReadDto.class);
    }

    public void deleteMovie(UUID id) {
        movieRepository.delete(repositoryHelper.getEntityRequired(Movie.class, id));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateAverageRatingOfMovie(UUID id) {
        Double avgRating = movieRepository.calcAverageRating(id);
        Movie movie = repositoryHelper.getEntityRequired(Movie.class, id);
        log.info("Setting new average rating of movie: {}. Old value: {}, new value: {}",
                id, movie.getRating(), avgRating);
        movie.setRating(avgRating);

        movieRepository.save(movie);
    }
}
