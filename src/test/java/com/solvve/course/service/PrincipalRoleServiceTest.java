package com.solvve.course.service;

import com.solvve.course.BaseTest;
import com.solvve.course.domain.Principal;
import com.solvve.course.domain.PrincipalRole;
import com.solvve.course.domain.constant.Role;
import com.solvve.course.dto.role.PrincipalRoleReadDto;
import com.solvve.course.exception.EntityNotFoundException;
import com.solvve.course.exception.LinkDuplicatedException;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertTrue;

public class PrincipalRoleServiceTest extends BaseTest {

    @Test
    public void testGetAllRoles() {
        assertThat(principalRoleService.getAllRoles()).extracting("role")
                .containsExactlyInAnyOrder(
                        Role.ADMIN,
                        Role.CONTENT_MANAGER,
                        Role.USER,
                        Role.GUEST,
                        Role.MODERATOR
                );
    }

    @Test
    public void testAddRoleToPrincipal() {
        Principal principal = utils.getPrincipalFromDb();
        PrincipalRole adminRole = principalRoleRepository.getByRole(Role.ADMIN);
        assertTrue(principal.getRoles().isEmpty());

        List<PrincipalRoleReadDto> actualRoles =
                principalRoleService.addRoleToPrincipal(principal.getId(), adminRole.getId());
        assertThat(actualRoles)
                .containsExactly(translationService.translate(adminRole, PrincipalRoleReadDto.class));

        utils.inTransaction(() -> {
            Principal principalAfterSave = principalRepository.findById(principal.getId()).get();
            assertThat(principalAfterSave.getRoles())
                    .extracting(PrincipalRole::getId).containsExactly(adminRole.getId());
        });
    }

    @Test
    public void testAddRoleToPrincipalWithSameRole() {
        Principal principal = utils.getPrincipalFromDb();
        PrincipalRole adminRole = principalRoleRepository.getByRole(Role.ADMIN);
        assertTrue(principal.getRoles().isEmpty());

        principalRoleService.addRoleToPrincipal(principal.getId(), adminRole.getId());
        assertThatThrownBy(() -> {
            principalRoleService.addRoleToPrincipal(principal.getId(), adminRole.getId());
        }).isInstanceOf(LinkDuplicatedException.class);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testAddNonexistentRoleToPrincipal() {
        Principal principal = utils.getPrincipalFromDb();

        principalRoleService.addRoleToPrincipal(principal.getId(), UUID.randomUUID());
    }

    @Test(expected = EntityNotFoundException.class)
    public void testAddRoleToNonexistentPrincipal() {
        PrincipalRole adminRole = principalRoleRepository.getByRole(Role.USER);

        principalRoleService.addRoleToPrincipal(UUID.randomUUID(), adminRole.getId());
    }
}