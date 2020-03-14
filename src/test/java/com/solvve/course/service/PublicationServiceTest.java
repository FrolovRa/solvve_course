package com.solvve.course.service;

import com.solvve.course.domain.Principal;
import com.solvve.course.domain.Publication;
import com.solvve.course.dto.publication.PublicationCreateDto;
import com.solvve.course.dto.publication.PublicationPatchDto;
import com.solvve.course.dto.publication.PublicationReadDto;
import com.solvve.course.exception.EntityNotFoundException;
import com.solvve.course.repository.PublicationRepository;
import com.solvve.course.util.TestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(statements = {
        "delete from publication",
        "delete from principal"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class PublicationServiceTest {

    @Autowired
    private PublicationRepository publicationRepository;

    @Autowired
    private TestUtils utils;

    @Autowired
    private TranslationService translationService;

    @Autowired
    private PublicationService publicationService;

    @Test
    public void testGetPublication() {
        Publication user = utils.getPublicationFromDb();
        PublicationReadDto actualPublication = translationService.toReadDto(user);

        PublicationReadDto publicationReadDto = publicationService.getPublication(user.getId());

        assertThat(actualPublication).isEqualToComparingFieldByField(publicationReadDto);
    }

    @Test
    public void testAddPublication() {
        Principal manager = utils.getPrincipalFromDb();
        PublicationCreateDto createDto = utils.createPublicationCreateDto();
        createDto.setManagerId(manager.getId());

        utils.inTransaction(() -> {
            PublicationReadDto readDto = publicationService.addPublication(createDto);

            assertThat(createDto).isEqualToIgnoringGivenFields(readDto,
                    "managerId", "updatedAt", "createdAt");
            assertNotNull(readDto.getId());
            assertEquals(readDto.getManager().getId(), createDto.getManagerId());

            PublicationReadDto userFromDb = publicationService.getPublication(readDto.getId());
            assertThat(readDto).isEqualToIgnoringGivenFields(userFromDb, "managerId");
            assertEquals(readDto.getManager(), userFromDb.getManager());
        });
    }

    @Test
    public void testPatchPublication() {
        Principal manager = utils.getPrincipalFromDb();
        Publication publication = utils.getPublicationFromDb();

        PublicationPatchDto publicationPatchDto = new PublicationPatchDto();
        publicationPatchDto.setTitle("Test");
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

        assertThat(translationService.toReadDto(publication)).isEqualToComparingFieldByField(patchedPublication);
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
        publication.setTitle("title");
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
        publication.setTitle("title");
        publication.setManager(principal);

        publication = publicationRepository.save(publication);

        Instant updatedAtBeforeReload = publication.getCreatedAt();
        assertNotNull(updatedAtBeforeReload);
        publication = publicationRepository.findById(publication.getId()).get();

        Instant updatedAtAfterReload = publication.getCreatedAt();
        assertNotNull(updatedAtAfterReload);
        assertEquals(updatedAtBeforeReload, updatedAtAfterReload);

        publication.setTitle("title updated");
        publication = publicationRepository.save(publication);
        Instant updatedAtAfterUpdate = publication.getUpdatedAt();

        assertNotEquals(updatedAtAfterUpdate, updatedAtAfterReload);
    }
}
