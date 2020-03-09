package com.solvve.course.job;

import com.solvve.course.repository.MovieRepository;
import com.solvve.course.service.MovieService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class UpdateAverageRatingOfMoviesJob {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private MovieService movieService;

    @Transactional(readOnly = true)
    @Scheduled(cron = "${update.average.rating.of.movies.job.cron}")
    public void updateAverageRatingOfMovies() {
        log.info("job started");

        movieRepository.getIdsOfAllMovies().forEach(movieId -> {
            try {
                movieService.updateAverageRatingOfMovie(movieId);
            } catch (Exception e) {
                log.error("Failed to update average rating for movie: {}", movieId, e);
            }
        });

        log.info("job finished");
    }
}
