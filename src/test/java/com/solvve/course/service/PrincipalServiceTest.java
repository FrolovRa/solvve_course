package com.solvve.course.service;

import com.solvve.course.BaseTest;
import com.solvve.course.domain.Principal;
import com.solvve.course.dto.principal.PrincipalCreateDto;
import com.solvve.course.dto.principal.PrincipalPatchDto;
import com.solvve.course.dto.principal.PrincipalReadDto;
import com.solvve.course.exception.EntityNotFoundException;
import org.junit.Test;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

public class PrincipalServiceTest extends BaseTest {

    @Test
    public void testGetPrincipal() {
        Principal user = utils.getPrincipalFromDb();
        PrincipalReadDto actualPrincipal = translationService.translate(user, PrincipalReadDto.class);

        PrincipalReadDto principalReadDto = principalService.getPrincipal(user.getId());

        assertThat(actualPrincipal).isEqualToComparingFieldByField(principalReadDto);
    }

    @Test
    public void testAddPrincipal() {
        PrincipalCreateDto createDto = utils.createPrincipalCreateDto();

        PrincipalReadDto readDto = principalService.addPrincipal(createDto);

        assertThat(createDto).isEqualToComparingFieldByField(readDto);
        assertNotNull(readDto.getId());

        PrincipalReadDto userFromDb = principalService.getPrincipal(readDto.getId());
        assertThat(readDto).isEqualToComparingFieldByField(userFromDb);
    }

    @Test
    public void testPatchPrincipal() {
        PrincipalPatchDto principalPatchDto = utils.createPrincipalPatchDto();

        Principal principal = utils.getPrincipalFromDb();

        PrincipalReadDto patchedPrincipal = principalService.patchPrincipal(principal.getId(), principalPatchDto);

        assertThat(principalPatchDto).isEqualToComparingFieldByField(patchedPrincipal);
    }

    @Test
    public void testEmptyPatchPrincipal() {
        PrincipalPatchDto principalPatchDto = new PrincipalPatchDto();
        Principal principal = utils.getPrincipalFromDb();

        PrincipalReadDto patchedPrincipal = principalService.patchPrincipal(principal.getId(), principalPatchDto);

        assertThat(principal).isEqualToIgnoringGivenFields(patchedPrincipal, "roles");
    }

    @Test
    public void testDeletePrincipal() {
        Principal principal = utils.getPrincipalFromDb();

        principalService.deletePrincipal(principal.getId());

        assertFalse(principalRepository.existsById(principal.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteByWrongId() {
        principalService.deletePrincipal(UUID.randomUUID());
    }

    @Test
    public void testCreatedAtIsSet() {
        Principal principal = utils.getPrincipalFromDb();

        principal = principalRepository.save(principal);

        Instant createdAtBeforeReload = principal.getCreatedAt();
        assertNotNull(createdAtBeforeReload);
        principal = principalRepository.findById(principal.getId()).get();

        Instant createdAtAfterReload = principal.getCreatedAt();
        assertNotNull(createdAtAfterReload);
        assertEquals(createdAtBeforeReload, createdAtAfterReload);
    }

    @Test
    public void testUpdatedAtIsSet() {
        Principal principal = utils.getPrincipalFromDb();

        principal = principalRepository.save(principal);

        Instant updatedAtBeforeReload = principal.getCreatedAt();
        assertNotNull(updatedAtBeforeReload);
        principal = principalRepository.findById(principal.getId()).get();

        Instant updatedAtAfterReload = principal.getCreatedAt();
        assertNotNull(updatedAtAfterReload);
        assertEquals(updatedAtBeforeReload, updatedAtAfterReload);

        principal.setEmail("@@");
        principal = principalRepository.save(principal);
        Instant updatedAtAfterUpdate = principal.getUpdatedAt();

        assertNotEquals(updatedAtAfterUpdate, updatedAtAfterReload);
    }
}