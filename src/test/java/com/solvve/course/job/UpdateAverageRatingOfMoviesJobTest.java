package com.solvve.course.job;

import com.solvve.course.domain.Movie;
import com.solvve.course.domain.Rating;
import com.solvve.course.repository.MovieRepository;
import com.solvve.course.repository.RatingRepository;
import com.solvve.course.service.MovieService;
import com.solvve.course.util.TestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(statements = {
        "delete from rating",
        "delete from movie"
}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class UpdateAverageRatingOfMoviesJobTest {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private TestUtils utils;

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