package com.solvve.course.job;

import com.solvve.course.BaseTest;
import com.solvve.course.domain.Movie;
import com.solvve.course.domain.Rating;
import com.solvve.course.service.MovieService;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;

public class UpdateAverageRatingOfMoviesJobTest extends BaseTest {

    @Autowired
    private UpdateAverageRatingOfMoviesJob updateAverageRatingOfMoviesJob;

    @SpyBean
    private MovieService movieService;

    @Test
    public void testUpdateAverageRatingOfMovies() {
        Movie movie = utils.getMovieFromDb();

        Rating rating = new Rating();
        rating.setUser(utils.getUserFromDb());
        rating.setEntityId(movie.getId());
        rating.setRating(3.4d);
        ratingRepository.save(rating);

        Rating secondRating = new Rating();
        secondRating.setUser(utils.getUserFromDb());
        secondRating.setEntityId(movie.getId());
        secondRating.setRating(0.5d);
        ratingRepository.save(secondRating);

        updateAverageRatingOfMoviesJob.updateAverageRatingOfMovies();

        movie = movieRepository.findById(movie.getId()).get();
        assertEquals(1.95, movie.getRating(), Double.MIN_VALUE);
    }

    @Test
    public void testUpdateAverageRatingOfMoviesIndependently() {
        Movie movie = utils.getMovieFromDb();

        Rating rating = new Rating();
        rating.setUser(utils.getUserFromDb());
        rating.setEntityId(movie.getId());
        rating.setRating(2.4d);
        ratingRepository.save(rating);

        Movie secondMovie = utils.getMovieFromDb();

        Rating ratingForSecondMovie = new Rating();
        ratingForSecondMovie.setUser(utils.getUserFromDb());
        ratingForSecondMovie.setEntityId(secondMovie.getId());
        ratingForSecondMovie.setRating(1.4d);
        ratingRepository.save(ratingForSecondMovie);

        UUID[] failedId = new UUID[1];
        Mockito.doAnswer(invocation -> {
            if (failedId[0] == null) {
                failedId[0] = invocation.getArgument(0);
                throw new RuntimeException();
            }
            return invocation.callRealMethod();
        }).when(movieService).updateAverageRatingOfMovie(any());

        updateAverageRatingOfMoviesJob.updateAverageRatingOfMovies();

        movieRepository.findAll().forEach(m -> {
            if (m.getId().equals(failedId[0])) {
                assertNull(m.getRating());
            } else {
                assertNotNull(m.getRating());
            }
        });
    }
}