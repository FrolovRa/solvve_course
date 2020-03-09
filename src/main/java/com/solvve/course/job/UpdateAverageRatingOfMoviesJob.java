package com.solvve.course.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UpdateAverageRatingOfMoviesJob {

    @Scheduled(cron = "${update.average.rating.of.movies.job.cron}")
    public void updateAverageRatingOfMovies() {
        log.info("job started");
        log.info("job finished");
    }
}
