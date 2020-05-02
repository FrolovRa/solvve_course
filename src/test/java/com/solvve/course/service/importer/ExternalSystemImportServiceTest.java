package com.solvve.course.service.importer;

import com.solvve.course.BaseTest;
import com.solvve.course.domain.ExternalSystemImport;
import com.solvve.course.domain.Movie;
import com.solvve.course.domain.constant.ImportedEntityType;
import com.solvve.course.exception.ImportAlreadyPerformedException;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ExternalSystemImportServiceTest extends BaseTest {

    @Test
    public void testValidateNotImported() throws ImportAlreadyPerformedException {
        externalSystemImportService.validateNotImported(Movie.class, "100");
    }

    @Test
    public void testExceptionWhenAlreadyImported() {
        ExternalSystemImport esi = utils.generateFlatEntityWithoutId(ExternalSystemImport.class);
        esi.setEntityType(ImportedEntityType.MOVIE);
        esi = externalSystemImportRepository.save(esi);
        final String idInExternalSystem = esi.getIdInExternalSystem();

        ImportAlreadyPerformedException exception = Assertions.catchThrowableOfType(
            () -> externalSystemImportService.validateNotImported(Movie.class, idInExternalSystem),
            ImportAlreadyPerformedException.class
        );

        assertThat(exception.getExternalSystemImport()).isEqualToComparingFieldByField(esi);
    }

    @Test
    public void testNoExceptionWhenAlreadyImportedButDifferentEntityType() throws ImportAlreadyPerformedException {
        ExternalSystemImport esi = utils.generateFlatEntityWithoutId(ExternalSystemImport.class);
        esi.setEntityType(ImportedEntityType.ACTOR);
        esi = externalSystemImportRepository.save(esi);

        externalSystemImportService.validateNotImported(Movie.class, esi.getIdInExternalSystem());
    }

    @Test
    public void testCreateExternalSystemImport() {
        Movie movie = utils.getMovieFromDb();
        final String idInExternalSystem = "100";

        UUID importId = externalSystemImportService.createExternalSystemImport(movie, idInExternalSystem);
        assertNotNull(importId);

        ExternalSystemImport esi = externalSystemImportRepository.findById(importId).get();
        assertEquals(ImportedEntityType.MOVIE, esi.getEntityType());
        assertEquals(idInExternalSystem, esi.getIdInExternalSystem());
        assertEquals(movie.getId(), esi.getEntityId());
    }
}