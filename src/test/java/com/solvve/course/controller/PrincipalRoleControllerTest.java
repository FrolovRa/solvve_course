package com.solvve.course.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.solvve.course.BaseControllerTest;
import com.solvve.course.domain.constant.Role;
import com.solvve.course.dto.role.PrincipalRoleReadDto;
import com.solvve.course.service.PrincipalRoleService;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PrincipalRoleController.class)
public class PrincipalRoleControllerTest extends BaseControllerTest {

    @MockBean
    private PrincipalRoleService principalRoleService;

    @Test
    public void testGetAllRoles() throws Exception {
        PrincipalRoleReadDto dto = new PrincipalRoleReadDto();
        dto.setRole(Role.MODERATOR);
        dto.setId(UUID.randomUUID());
        List<PrincipalRoleReadDto> expected = Collections.singletonList(dto);
        when(principalRoleService.getAllRoles()).thenReturn(expected);

        String resultJson = mvc.perform(get("/api/v1/roles"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<PrincipalRoleReadDto> actual = objectMapper.readValue(resultJson, new TypeReference<>() {
        });
        assertEquals(expected, actual);
    }

    @Test
    public void testAddRoleToPrincipal() throws Exception {
        final UUID principalId = UUID.randomUUID();
        final UUID roleId = UUID.randomUUID();

        PrincipalRoleReadDto dto = new PrincipalRoleReadDto();
        dto.setRole(Role.MODERATOR);
        dto.setId(principalId);
        List<PrincipalRoleReadDto> expected = Collections.singletonList(dto);
        when(principalRoleService.addRoleToPrincipal(principalId, roleId)).thenReturn(expected);

        String resultJson =
                mvc.perform(post("/api/v1/principals/{principalId}/roles/{roleId}", principalId, roleId))
                        .andExpect(status().isOk())
                        .andReturn().getResponse().getContentAsString();

        List<PrincipalRoleReadDto> actual = objectMapper.readValue(resultJson, new TypeReference<>() {
        });
        assertEquals(expected, actual);
    }
}
