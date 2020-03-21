package com.solvve.course.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solvve.course.domain.Correction;
import com.solvve.course.dto.correction.CorrectionReadDto;
import com.solvve.course.exception.EntityNotFoundException;
import com.solvve.course.service.CorrectionService;
import com.solvve.course.util.TestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = CorrectionController.class)
public class CorrectionControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CorrectionService correctionService;

    private TestUtils utils = new TestUtils();

    @Test
    public void testGetAllCorrections() throws Exception {
        List<CorrectionReadDto> expected = Collections.singletonList(utils.createCorrectionReadDto());
        when(correctionService.getAllCorrections()).thenReturn(expected);

        String resultJson = mvc.perform(get("/api/v1/corrections"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        List<CorrectionReadDto> actual = objectMapper.readValue(resultJson, new TypeReference<>() {
        });

        assertEquals(expected, actual);
    }

    @Test
    public void testGetCorrection() throws Exception {
        CorrectionReadDto correctionReadDto = utils.createCorrectionReadDto();
        when(correctionService.getCorrection(correctionReadDto.getId())).thenReturn(correctionReadDto);

        String resultJson = mvc.perform(get("/api/v1/corrections/{id}", correctionReadDto.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        CorrectionReadDto actualCorrection = objectMapper.readValue(resultJson, CorrectionReadDto.class);

        assertEquals(actualCorrection, correctionReadDto);
    }

    @Test
    public void testGetCorrectionByWrongId() throws Exception {
        UUID wrongId = UUID.randomUUID();

        RuntimeException exception = new EntityNotFoundException(Correction.class, wrongId);
        when(correctionService.getCorrection(wrongId)).thenThrow(exception);

        String resultJson = mvc.perform(get("/api/v1/corrections/{id}", wrongId))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        assertTrue(resultJson.contains(exception.getMessage()));
    }

    @Test
    public void testGetCorrectionWithNotValidId() throws Exception {
        mvc.perform(get("/api/v1/corrections/{id}", 42)).andExpect(status().isBadRequest());

        verifyNoInteractions(correctionService);
    }

    @Test
    public void testDeleteCorrection() throws Exception {
        UUID id = UUID.randomUUID();

        mvc.perform(delete("/api/v1/corrections/{id}", id)).andExpect(status().isOk());

        verify(correctionService).deleteCorrection(id);
    }
}