package com.solvve.course.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.solvve.course.domain.Principal;
import com.solvve.course.domain.constant.Role;
import com.solvve.course.dto.principal.PrincipalCreateDto;
import com.solvve.course.dto.principal.PrincipalPatchDto;
import com.solvve.course.dto.principal.PrincipalReadDto;
import com.solvve.course.exception.EntityNotFoundException;
import com.solvve.course.service.PrincipalService;
import com.solvve.course.util.TestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = PrincipalController.class)
public class PrincipalControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PrincipalService principalService;

    private TestUtils utils = new TestUtils();

    @Test
    public void testGetPrincipal() throws Exception {
        PrincipalReadDto principalReadDto = utils.createPrincipalReadDto();
        when(principalService.getPrincipal(principalReadDto.getId())).thenReturn(principalReadDto);

        String resultJson = mvc.perform(get("/api/v1/principals/{id}", principalReadDto.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        PrincipalReadDto actualPrincipal = objectMapper.readValue(resultJson, PrincipalReadDto.class);

        assertEquals(actualPrincipal, principalReadDto);
    }

    @Test
    public void testGetPrincipalByWrongId() throws Exception {
        UUID wrongId = UUID.randomUUID();

        RuntimeException exception = new EntityNotFoundException(Principal.class, wrongId);
        when(principalService.getPrincipal(wrongId)).thenThrow(exception);

        String resultJson = mvc.perform(get("/api/v1/principals/{id}", wrongId))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        assertTrue(resultJson.contains(exception.getMessage()));
    }

    @Test
    public void testGetPrincipalsByRole() throws Exception {
        List<PrincipalReadDto> expected = Collections.singletonList(utils.createPrincipalReadDto());

        when(principalService.getPrincipalsByRole(Role.USER)).thenReturn(expected);

        String resultJson = mvc.perform(get("/api/v1/principals/by-role/{role}", Role.USER))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<PrincipalReadDto> actual = Arrays.asList(objectMapper.readValue(resultJson, PrincipalReadDto[].class));
        assertEquals(expected, actual);
    }

    @Test
    public void testGetPrincipalByRoleWithNotValidRole() throws Exception {
        mvc.perform(get("/api/v1/principals/by-role/{role}", "bad"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(principalService);
    }

    @Test
    public void testGetPrincipalWithNotValidId() throws Exception {
        mvc.perform(get("/api/v1/principals/{id}", 42)).andExpect(status().isBadRequest());

        verifyNoInteractions(principalService);
    }

    @Test
    public void testAddPrincipal() throws Exception {
        PrincipalCreateDto principalCreateDto = utils.createPrincipalCreateDto();
        PrincipalReadDto principalReadDto = utils.createPrincipalReadDto();

        when(principalService.addPrincipal(principalCreateDto)).thenReturn(principalReadDto);

        String resultJson = mvc.perform(post("/api/v1/principals")
                .content(objectMapper.writeValueAsString(principalCreateDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        PrincipalReadDto actualPrincipalReadDto = objectMapper.readValue(resultJson, PrincipalReadDto.class);
        assertThat(actualPrincipalReadDto).isEqualToComparingFieldByField(principalReadDto);
    }

    @Test
    public void testPatchPrincipal() throws Exception {
        UUID id = UUID.randomUUID();
        PrincipalPatchDto principalPatchDto = utils.createPrincipalPatchDto();

        PrincipalReadDto principalReadDto = utils.createPrincipalReadDto();

        when(principalService.patchPrincipal(id, principalPatchDto)).thenReturn(principalReadDto);

        String resultJson = mvc.perform(patch("/api/v1/principals/{id}", id)
                .content(objectMapper.writeValueAsString(principalPatchDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        PrincipalReadDto actualPrincipalReadDto = objectMapper.readValue(resultJson, PrincipalReadDto.class);
        assertThat(actualPrincipalReadDto).isEqualToComparingFieldByField(principalReadDto);
    }

    @Test
    public void testDeletePrincipal() throws Exception {
        UUID id = UUID.randomUUID();

        mvc.perform(delete("/api/v1/principals/{id}", id)).andExpect(status().isOk());

        verify(principalService).deletePrincipal(id);
    }
}