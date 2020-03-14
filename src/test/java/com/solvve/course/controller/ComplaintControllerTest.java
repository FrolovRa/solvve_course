package com.solvve.course.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.solvve.course.domain.Complaint;
import com.solvve.course.dto.complaint.ComplaintCreateDto;
import com.solvve.course.dto.complaint.ComplaintReadDto;
import com.solvve.course.exception.EntityNotFoundException;
import com.solvve.course.service.ComplaintService;
import com.solvve.course.util.TestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = ComplaintController.class)
public class ComplaintControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ComplaintService complaintService;

    private TestUtils utils = new TestUtils();

    @Test
    public void testGetComplaint() throws Exception {
        ComplaintReadDto complaintReadDto = utils.createComplaintReadDto();
        when(complaintService.getComplaint(complaintReadDto.getId())).thenReturn(complaintReadDto);

        String resultJson = mvc.perform(get("/api/v1/complaints/{id}", complaintReadDto.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        ComplaintReadDto actualComplaint = objectMapper.readValue(resultJson, ComplaintReadDto.class);

        assertEquals(actualComplaint, complaintReadDto);
    }

    @Test
    public void testGetComplaintByWrongId() throws Exception {
        UUID wrongId = UUID.randomUUID();

        RuntimeException exception = new EntityNotFoundException(Complaint.class, wrongId);
        when(complaintService.getComplaint(wrongId)).thenThrow(exception);

        String resultJson = mvc.perform(get("/api/v1/complaints/{id}", wrongId))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        assertTrue(resultJson.contains(exception.getMessage()));
    }

    @Test
    public void testGetComplaintWithNotValidId() throws Exception {
        mvc.perform(get("/api/v1/complaints/{id}", 42)).andExpect(status().isBadRequest());

        verifyNoInteractions(complaintService);
    }

    @Test
    public void testAddComplaint() throws Exception {
        ComplaintCreateDto complaintCreateDto = utils.createComplaintCreateDto();
        ComplaintReadDto complaintReadDto = utils.createComplaintReadDto();

        when(complaintService.addComplaint(complaintCreateDto)).thenReturn(complaintReadDto);

        String resultJson = mvc.perform(post("/api/v1/complaints")
                .content(objectMapper.writeValueAsString(complaintCreateDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        ComplaintReadDto actualComplaintReadDto = objectMapper.readValue(resultJson, ComplaintReadDto.class);
        assertThat(actualComplaintReadDto).isEqualToComparingFieldByField(complaintReadDto);
    }

    @Test
    public void testDeleteComplaint() throws Exception {
        UUID id = UUID.randomUUID();

        mvc.perform(delete("/api/v1/complaints/{id}", id)).andExpect(status().isOk());

        verify(complaintService).deleteComplaint(id);
    }
}