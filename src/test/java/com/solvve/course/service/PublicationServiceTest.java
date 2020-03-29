package com.solvve.course.service;

import com.solvve.course.BaseTest;
import com.solvve.course.domain.Principal;
import com.solvve.course.domain.Publication;
import com.solvve.course.dto.publication.PublicationCreateDto;
import com.solvve.course.dto.publication.PublicationPatchDto;
import com.solvve.course.dto.publication.PublicationReadDto;
import com.solvve.course.exception.EntityNotFoundException;
import org.junit.Test;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

public class PublicationServiceTest extends BaseTest {

    @Test
    public void testGetPublication() {
        Publication user = utils.getPublicationFromDb();
        PublicationReadDto actualPublication = translationService.translate(user, PublicationReadDto.class);

        PublicationReadDto publicationReadDto = publicationService.getPublication(user.getId());

        assertThat(actualPublication).isEqualToComparingFieldByField(publicationReadDto);
    }

    @Test
    public void testAddPublication() {
        Principal manager = utils.getPrincipalFromDb();
        PublicationCreateDto createDto = utils.createPublicationCreateDto();
        createDto.setManagerId(manager.getId());

        PublicationReadDto readDto = publicationService.addPublication(createDto);
        assertThat(createDto).isEqualToIgnoringGivenFields(readDto,
            "managerId", "updatedAt", "createdAt");
        assertNotNull(readDto.getId());
        assertEquals(readDto.getManager().getId(), createDto.getManagerId());

        PublicationReadDto publicationFromDb = publicationService.getPublication(readDto.getId());
        assertThat(readDto).isEqualToIgnoringGivenFields(publicationFromDb, "managerId");
        assertEquals(readDto.getManager(), publicationFromDb.getManager());
    }

    @Test
    public void testPatchPublication() {
        Principal manager = utils.getPrincipalFromDb();
        Publication publication = utils.getPublicationFromDb();

        PublicationPatchDto publicationPatchDto = new PublicationPatchDto();
        publicationPatchDto.setContent("Test");
        publicationPatchDto.setManagerId(manager.getId());

        PublicationReadDto patchedPublication = publicationService.patchPublication(publication.getId(), publicationPatchDto);

        assertThat(publicationPatchDto).isEqualToIgnoringGivenFields(patchedPublication,
            "managerId", "updatedAt", "createdAt");
        assertEquals(patchedPublication.getManager().getId(), publicationPatchDto.getManagerId());
    }

    @Test
    public void testEmptyPatchPublication() {
        PublicationPatchDto publicationPatchDto = new PublicationPatchDto();
        Publication publication = utils.getPublicationFromDb();

        PublicationReadDto patchedPublication = publicationService.patchPublication(publication.getId(), publicationPatchDto);

        assertThat(patchedPublication).isEqualToIgnoringGivenFields(publication, "manager");
        assertThat(patchedPublication.getManager()).isEqualToComparingFieldByField(publication.getManager());
    }

    @Test
    public void testDeletePublication() {
        Publication publication = utils.getPublicationFromDb();

        publicationService.deletePublication(publication.getId());

        assertFalse(publicationRepository.existsById(publication.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteByWrongId() {
        publicationService.deletePublication(UUID.randomUUID());
    }

    @Test
    public void testCreatedAtIsSet() {
        Principal principal = utils.getPrincipalFromDb();

        Publication publication = new Publication();
        publication.setManager(principal);

        publication = publicationRepository.save(publication);

        Instant createdAtBeforeReload = publication.getCreatedAt();
        assertNotNull(createdAtBeforeReload);
        publication = publicationRepository.findById(publication.getId()).get();

        Instant createdAtAfterReload = publication.getCreatedAt();
        assertNotNull(createdAtAfterReload);
        assertEquals(createdAtBeforeReload, createdAtAfterReload);
    }

    @Test
    public void testUpdatedAtIsSet() {
        Principal principal = utils.getPrincipalFromDb();

        Publication publication = new Publication();
        publication.setManager(principal);
        publication.setContent("test");

        publication = publicationRepository.save(publication);

        Instant updatedAtBeforeReload = publication.getCreatedAt();
        assertNotNull(updatedAtBeforeReload);
        publication = publicationRepository.findById(publication.getId()).get();

        Instant updatedAtAfterReload = publication.getCreatedAt();
        assertNotNull(updatedAtAfterReload);
        assertEquals(updatedAtBeforeReload, updatedAtAfterReload);

        publication.setContent("test 2");
        publication = publicationRepository.save(publication);
        Instant updatedAtAfterUpdate = publication.getUpdatedAt();

        assertNotEquals(updatedAtAfterUpdate, updatedAtAfterReload);
    }
}
