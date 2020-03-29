package com.solvve.course.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.solvve.course.BaseControllerTest;
import com.solvve.course.domain.Publication;
import com.solvve.course.dto.correction.CorrectionCreateDto;
import com.solvve.course.dto.correction.CorrectionPatchDto;
import com.solvve.course.dto.correction.CorrectionReadDto;
import com.solvve.course.dto.publication.PublicationReadDto;
import com.solvve.course.exception.EntityNotFoundException;
import com.solvve.course.service.PublicationCorrectionService;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PublicationCorrectionController.class)
public class PublicationCorrectionControllerTest extends BaseControllerTest {

    @MockBean
    private PublicationCorrectionService publicationCorrectionService;

    @Test
    public void testGetPublicationCorrections() throws Exception {
        CorrectionReadDto correctionReadDto = utils.createCorrectionReadDto();
        UUID publicationId = correctionReadDto.getPublication().getId();
        when(publicationCorrectionService.getPublicationCorrections(publicationId))
                .thenReturn(Collections.singletonList(correctionReadDto));

        String resultJson = mvc.perform(get("/api/v1/publications/{publicationId}/corrections", publicationId))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        List<CorrectionReadDto> actualCorrection = objectMapper.readValue(resultJson, new TypeReference<>() {
        });

        assertEquals(actualCorrection, Collections.singletonList(correctionReadDto));
    }

    @Test
    public void testGetPublicationCorrectionsByWrongId() throws Exception {
        UUID wrongId = UUID.randomUUID();

        RuntimeException exception = new EntityNotFoundException(Publication.class, wrongId);
        when(publicationCorrectionService.getPublicationCorrections(wrongId)).thenThrow(exception);

        String resultJson = mvc.perform(get("/api/v1/publications/{publicationId}/corrections",
                wrongId))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        assertTrue(resultJson.contains(exception.getMessage()));
    }

    @Test
    public void testGetPublicationCorrectionsWithNotValidId() throws Exception {
        mvc.perform(get("/api/v1/publications/{publicationId}/corrections", 42))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(publicationCorrectionService);
    }

    @Test
    public void testAddCorrection() throws Exception {
        CorrectionCreateDto correctionCreateDto = utils.createCorrectionCreateDto();
        CorrectionReadDto correctionReadDto = utils.createCorrectionReadDto();
        UUID publicationId = correctionReadDto.getPublication().getId();

        when(publicationCorrectionService.addPublicationCorrection(publicationId, correctionCreateDto))
                .thenReturn(correctionReadDto);

        String resultJson = mvc.perform(post("/api/v1/publications/{publicationId}/corrections", publicationId)
                .content(objectMapper.writeValueAsString(correctionCreateDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        CorrectionReadDto actualCorrectionReadDto = objectMapper.readValue(resultJson, CorrectionReadDto.class);
        assertThat(actualCorrectionReadDto).isEqualToComparingFieldByField(correctionReadDto);
    }

    @Test
    public void testAcceptCorrection() throws Exception {
        final UUID correctionId = UUID.randomUUID();
        PublicationReadDto publication = utils.createPublicationReadDto();

        CorrectionPatchDto patchDto = new CorrectionPatchDto();
        patchDto.setProposedText("text");

        when(publicationCorrectionService.acceptPublicationCorrection(correctionId, publication.getId(), patchDto))
                .thenReturn(publication);

        String resultJson =
                mvc.perform(patch("/api/v1/publications/{publicationId}/corrections/{correctionId}/accept",
                        publication.getId(), correctionId)
                        .content(objectMapper.writeValueAsString(patchDto))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn().getResponse().getContentAsString();

        PublicationReadDto actual = objectMapper.readValue(resultJson, PublicationReadDto.class);
        assertThat(actual).isEqualToComparingFieldByField(publication);
    }

    @Test
    public void testDeleteCorrection() throws Exception {
        UUID id = UUID.randomUUID();

        mvc.perform(delete("/api/v1/publications/{publicationId}/corrections/{id}", id, id))
                .andExpect(status().isOk());

        verify(publicationCorrectionService).deletePublicationCorrection(id);
    }
}