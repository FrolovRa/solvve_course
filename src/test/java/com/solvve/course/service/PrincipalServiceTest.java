package com.solvve.course.service;

import com.solvve.course.domain.Principal;
import com.solvve.course.domain.constant.Role;
import com.solvve.course.dto.principal.PrincipalCreateDto;
import com.solvve.course.dto.principal.PrincipalPatchDto;
import com.solvve.course.dto.principal.PrincipalReadDto;
import com.solvve.course.exception.EntityNotFoundException;
import com.solvve.course.repository.PrincipalRepository;
import com.solvve.course.util.TestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

import javax.transaction.Transactional;
import java.util.UUID;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(statements = {
        "delete from user",
        "delete from principal"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class PrincipalServiceTest {
    @Autowired
    private PrincipalRepository principalRepository;
    @Autowired
    private TestUtils utils;
    @Autowired
    private TranslationService translationService;
    @Autowired
    private PrincipalService principalService;

    @Test
    @Transactional
    public void testGetPrincipal() {
        Principal user = utils.getPrincipalFromDb();
        PrincipalReadDto actualPrincipal = translationService.toReadDto(user);

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
        PrincipalPatchDto principalPatchDto = new PrincipalPatchDto();
        principalPatchDto.setName("Test");
        principalPatchDto.setBlocked(true);
        principalPatchDto.setRole(Role.ADMIN);
        principalPatchDto.setEmail("test@maol.ss");

        Principal principal = utils.getPrincipalFromDb();

        PrincipalReadDto patchedPrincipal = principalService.patchPrincipal(principal.getId(), principalPatchDto);

        assertThat(principalPatchDto).isEqualToComparingFieldByField(patchedPrincipal);
    }

    @Test
    public void testEmptyPatchPrincipal() {
        PrincipalPatchDto principalPatchDto = new PrincipalPatchDto();
        Principal principal = utils.getPrincipalFromDb();

        PrincipalReadDto patchedPrincipal = principalService.patchPrincipal(principal.getId(), principalPatchDto);

        assertThat(translationService.toReadDto(principal)).isEqualToComparingFieldByField(patchedPrincipal);
    }

    @Test
    @Transactional
    public void testDeletePrincipal() {
        Principal principal = utils.getPrincipalFromDb();

        principalService.deleteUser(principal.getId());

        assertFalse(principalRepository.existsById(principal.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteByWrongId() {
        principalService.deleteUser(UUID.randomUUID());
    }
}
