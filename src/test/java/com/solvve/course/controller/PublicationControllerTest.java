package com.solvve.course.controller;

import com.solvve.course.BaseControllerTest;
import com.solvve.course.domain.Publication;
import com.solvve.course.dto.publication.PublicationCreateDto;
import com.solvve.course.dto.publication.PublicationPatchDto;
import com.solvve.course.dto.publication.PublicationReadDto;
import com.solvve.course.exception.EntityNotFoundException;
import com.solvve.course.service.PublicationService;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PublicationController.class)
public class PublicationControllerTest extends BaseControllerTest {

    @MockBean
    private PublicationService publicationService;

    @Test
    public void testGetPublication() throws Exception {
        PublicationReadDto publicationReadDto = utils.createPublicationReadDto();
        when(publicationService.getPublication(publicationReadDto.getId())).thenReturn(publicationReadDto);

        String resultJson = mvc.perform(get("/api/v1/publications/{id}", publicationReadDto.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        PublicationReadDto actualPublication = objectMapper.readValue(resultJson, PublicationReadDto.class);

        assertEquals(actualPublication, publicationReadDto);
    }

    @Test
    public void testGetPublicationByWrongId() throws Exception {
        UUID wrongId = UUID.randomUUID();

        RuntimeException exception = new EntityNotFoundException(Publication.class, wrongId);
        when(publicationService.getPublication(wrongId)).thenThrow(exception);

        String resultJson = mvc.perform(get("/api/v1/publications/{id}", wrongId))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        assertTrue(resultJson.contains(exception.getMessage()));
    }

    @Test
    public void testGetPublicationWithNotValidId() throws Exception {
        mvc.perform(get("/api/v1/publications/{id}", 42)).andExpect(status().isBadRequest());

        verifyNoInteractions(publicationService);
    }

    @Test
    public void testAddPublication() throws Exception {
        PublicationCreateDto publicationCreateDto = utils.createPublicationCreateDto();
        PublicationReadDto publicationReadDto = utils.createPublicationReadDto();

        when(publicationService.addPublication(publicationCreateDto)).thenReturn(publicationReadDto);

        String resultJson = mvc.perform(post("/api/v1/publications")
                .content(objectMapper.writeValueAsString(publicationCreateDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        PublicationReadDto actualPublicationReadDto = objectMapper.readValue(resultJson, PublicationReadDto.class);
        assertThat(actualPublicationReadDto).isEqualToComparingFieldByField(publicationReadDto);
    }

    @Test
    public void testPatchPublication() throws Exception {
        UUID id = UUID.randomUUID();
        PublicationPatchDto publicationPatchDto = utils.createPublicationPatchDto();

        PublicationReadDto publicationReadDto = utils.createPublicationReadDto();

        when(publicationService.patchPublication(id, publicationPatchDto)).thenReturn(publicationReadDto);

        String resultJson = mvc.perform(patch("/api/v1/publications/{id}", id)
                .content(objectMapper.writeValueAsString(publicationPatchDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        PublicationReadDto actualPublicationReadDto = objectMapper.readValue(resultJson, PublicationReadDto.class);
        assertThat(actualPublicationReadDto).isEqualToComparingFieldByField(publicationReadDto);
    }

    @Test
    public void testDeletePublication() throws Exception {
        UUID id = UUID.randomUUID();

        mvc.perform(delete("/api/v1/publications/{id}", id)).andExpect(status().isOk());

        verify(publicationService).deletePublication(id);
    }
}