package com.solvve.course.job.oneoff;

import com.solvve.course.client.themoviedb.TheMovieDbClient;
import com.solvve.course.client.themoviedb.dto.MovieReadShortDto;
import com.solvve.course.exception.ImportAlreadyPerformedException;
import com.solvve.course.exception.ImportedEntityAlreadyExistException;
import com.solvve.course.service.AsyncService;
import com.solvve.course.service.importer.MovieImporterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Slf4j
@Component
public class TheMovieDbImportOneOffJob {

    @Autowired
    private TheMovieDbClient client;

    @Autowired
    private MovieImporterService movieImporterService;

    @Autowired
    private AsyncService asyncService;

    @Value("${themoviedb.import.enabled}")
    private boolean enabled;

    @PostConstruct
    void executeJob() {
        if (!enabled) {
            log.info("Import is disabled");
            return;
        }

        asyncService.executeAsync(this::doImport);
    }

    public void doImport() {
        log.info("import started");

        try {
            List<MovieReadShortDto> moviesToImport = client.getTopRatedMovies().getResults();
            int successfullyImported = 0;
            int skipped = 0;
            int failed = 0;

            for (MovieReadShortDto movie : moviesToImport) {
                try {
                    movieImporterService.importMovie(movie.getId());
                    successfullyImported++;
                } catch (ImportedEntityAlreadyExistException | ImportAlreadyPerformedException e) {
                    log.info("Can't import movie id={} title={}: {}", movie.getId(), movie.getTitle(), e.getMessage());
                    skipped++;
                } catch (Exception e) {
                    log.warn("Failed to import movie id={} title={}", movie.getId(), movie.getTitle(), e);
                    failed++;
                }
            }

            log.info("Total movies to import={}, successfully imported={}, skipped={}, failed={}",
                moviesToImport.size(), successfullyImported, skipped, failed);
        } catch (Exception e) {
            log.warn("Failed to perform import", e);
        }

        log.info("import finished");
    }
}
