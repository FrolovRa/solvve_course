package com.solvve.course.service;

import com.solvve.course.BaseTest;
import com.solvve.course.domain.Correction;
import com.solvve.course.domain.Publication;
import com.solvve.course.domain.constant.CorrectionStatus;
import com.solvve.course.dto.correction.CorrectionCreateDto;
import com.solvve.course.dto.correction.CorrectionPatchDto;
import com.solvve.course.dto.correction.CorrectionReadDto;
import com.solvve.course.dto.publication.PublicationReadDto;
import com.solvve.course.exception.BadCorrectionStatusException;
import com.solvve.course.exception.EntityNotFoundException;
import org.junit.Test;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

public class PublicationCorrectionServiceTest extends BaseTest {

    @Test
    public void testGetPublicationCorrections() {
        Correction publicationCorrection = utils.getCorrectionFromDb();
        List<CorrectionReadDto> actual = Collections
                .singletonList(translationService.translate(publicationCorrection, CorrectionReadDto.class));

        List<CorrectionReadDto> result = publicationCorrectionService
                .getPublicationCorrections(publicationCorrection.getPublication().getId());

        assertThat(actual).isEqualTo(result);
    }

    @Test
    public void testAddPublicationCorrection() {
        Publication publication = utils.getPublicationFromDb();
        CorrectionCreateDto createDto = utils.createCorrectionCreateDto();
        createDto.setUserId(utils.getUserFromDb().getId());
        createDto.setPublicationId(publication.getId());

        CorrectionReadDto readDto = publicationCorrectionService.addPublicationCorrection(publication.getId(), createDto);
        assertThat(createDto).isEqualToIgnoringGivenFields(readDto,
                "userId", "publicationId", "updatedAt", "createdAt");
        assertNotNull(readDto.getId());
        assertEquals(readDto.getUser().getId(), createDto.getUserId());
        assertEquals(readDto.getPublication().getId(), createDto.getPublicationId());

        List<CorrectionReadDto> corrections = publicationCorrectionService.getPublicationCorrections(publication.getId());
        assertThat(corrections.size()).isEqualTo(1);
        assertThat(corrections.get(0)).isEqualTo(readDto);
    }

    @Test
    public void testDeletePublicationCorrection() {
        Correction publicationCorrection = utils.getCorrectionFromDb();

        publicationCorrectionService.deletePublicationCorrection(publicationCorrection.getId());

        assertFalse(correctionRepository.existsById(publicationCorrection.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteByWrongId() {
        publicationCorrectionService.deletePublicationCorrection(UUID.randomUUID());
    }

    @Test
    public void testCreatedAtIsSet() {
        Correction publicationCorrection = utils.getCorrectionFromDb();

        publicationCorrection = correctionRepository.save(publicationCorrection);

        Instant createdAtBeforeReload = publicationCorrection.getCreatedAt();
        assertNotNull(createdAtBeforeReload);
        publicationCorrection = correctionRepository.findById(publicationCorrection.getId()).get();

        Instant createdAtAfterReload = publicationCorrection.getCreatedAt();
        assertNotNull(createdAtAfterReload);
        assertEquals(createdAtBeforeReload, createdAtAfterReload);
    }

    @Test
    public void testUpdatedAtIsSet() {
        Correction publicationCorrection = utils.getCorrectionFromDb();
        publicationCorrection.setStatus(CorrectionStatus.NEW);

        publicationCorrection = correctionRepository.save(publicationCorrection);

        Instant updatedAtBeforeReload = publicationCorrection.getCreatedAt();
        assertNotNull(updatedAtBeforeReload);
        publicationCorrection = correctionRepository.findById(publicationCorrection.getId()).get();

        Instant updatedAtAfterReload = publicationCorrection.getCreatedAt();
        assertNotNull(updatedAtAfterReload);
        assertEquals(updatedAtBeforeReload, updatedAtAfterReload);

        publicationCorrection.setStatus(CorrectionStatus.ON_REVIEW);
        publicationCorrection = correctionRepository.save(publicationCorrection);
        Instant updatedAtAfterUpdate = publicationCorrection.getUpdatedAt();

        assertNotEquals(updatedAtAfterUpdate, updatedAtAfterReload);
    }

    @Test
    public void testAcceptPublicationCorrection() {
        Correction publicationCorrection = utils.getCorrectionFromDb();
        Publication publication = utils.getPublicationFromDb();
        CorrectionPatchDto dto = new CorrectionPatchDto();

        Correction correctionBeforeMethod = correctionRepository.findById(publicationCorrection.getId()).get();
        PublicationReadDto publicationReadDto = publicationCorrectionService
                .acceptPublicationCorrection(publicationCorrection.getId(), publication.getId(), dto);
        assertEquals("fixed text content", publicationReadDto.getContent());

        Correction correctionDb = correctionRepository.findById(publicationCorrection.getId()).get();
        assertEquals(correctionBeforeMethod.getStatus(), CorrectionStatus.NEW);
        assertEquals(correctionBeforeMethod.getProposedText(), correctionDb.getProposedText());
        assertEquals(correctionDb.getStatus(), CorrectionStatus.ACCEPTED);
    }

    @Test
    public void testAcceptPublicationCorrectionWithContentManagerFix() {
        Correction publicationCorrection = utils.getCorrectionFromDb();
        Publication publication = publicationCorrection.getPublication();
        CorrectionPatchDto dto = new CorrectionPatchDto();
        dto.setProposedText("content manager fix");

        Correction correctionBeforeMethod = correctionRepository.findById(publicationCorrection.getId()).get();
        PublicationReadDto publicationReadDto = publicationCorrectionService
                .acceptPublicationCorrection(publicationCorrection.getId(), publication.getId(), dto);
        assertEquals("content manager fix content", publicationReadDto.getContent());

        Correction correctionDb = correctionRepository.findById(publicationCorrection.getId()).get();
        assertEquals(correctionBeforeMethod.getStatus(), CorrectionStatus.NEW);
        assertNotEquals(correctionBeforeMethod.getProposedText(), correctionDb.getProposedText());
        assertEquals(correctionDb.getStatus(), CorrectionStatus.ACCEPTED_AFTER_FIX);
    }

    @Test
    public void testAcceptPublicationCorrectionWithUpdateSimilar() throws InterruptedException {
        Correction publicationCorrection = utils.getCorrectionFromDb();
        Correction similarCorrection = utils.getCorrectionFromDb();
        Correction secondSimilarCorrection = utils.getCorrectionFromDb();
        Publication publication = publicationCorrection.getPublication();

        similarCorrection.setPublication(publication);
        secondSimilarCorrection.setPublication(publication);
        similarCorrection = correctionRepository.save(similarCorrection);
        secondSimilarCorrection = correctionRepository.save(secondSimilarCorrection);

        Correction correctionBeforeMethod = correctionRepository.findById(publicationCorrection.getId()).get();

        CorrectionPatchDto dto = new CorrectionPatchDto();
        dto.setProposedText("content manager fix");
        PublicationReadDto publicationReadDto = publicationCorrectionService
                .acceptPublicationCorrection(publicationCorrection.getId(), publication.getId(), dto);
        assertEquals("content manager fix content", publicationReadDto.getContent());

        Thread.sleep(250);
        Correction correctionDb = correctionRepository.findById(publicationCorrection.getId()).get();
        assertEquals(correctionBeforeMethod.getStatus(), CorrectionStatus.NEW);
        assertEquals(correctionDb.getStatus(), CorrectionStatus.ACCEPTED_AFTER_FIX);
        assertNotEquals(correctionBeforeMethod.getProposedText(), correctionDb.getProposedText());
        assertNotEquals(similarCorrection, correctionRepository.findById(similarCorrection.getId()));
        assertNotEquals(secondSimilarCorrection, correctionRepository.findById(secondSimilarCorrection.getId()));
    }

    @Test(expected = BadCorrectionStatusException.class)
    public void testAcceptPublicationCorrectionGetError() {
        Correction correction = utils.getCorrectionFromDb();
        correction.setStatus(CorrectionStatus.ACCEPTED);
        correction = correctionRepository.save(correction);

        publicationCorrectionService.acceptPublicationCorrection(correction.getId(),
                correction.getPublication().getId(),
                new CorrectionPatchDto());
    }
}