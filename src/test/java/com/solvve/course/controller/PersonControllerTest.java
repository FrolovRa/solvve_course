package com.solvve.course.controller;

import com.solvve.course.BaseControllerTest;
import com.solvve.course.domain.Person;
import com.solvve.course.dto.person.PersonCreateDto;
import com.solvve.course.dto.person.PersonPatchDto;
import com.solvve.course.dto.person.PersonReadDto;
import com.solvve.course.exception.EntityNotFoundException;
import com.solvve.course.service.PersonService;
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

@WebMvcTest(controllers = PersonController.class)
public class PersonControllerTest extends BaseControllerTest {

    @MockBean
    private PersonService personService;

    @Test
    public void testGetPerson() throws Exception {
        PersonReadDto personReadDto = utils.createPersonReadDto();
        when(personService.getPerson(personReadDto.getId())).thenReturn(personReadDto);

        String resultJson = mvc.perform(get("/api/v1/persons/{id}", personReadDto.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        PersonReadDto actualPerson = objectMapper.readValue(resultJson, PersonReadDto.class);

        assertEquals(actualPerson, personReadDto);
    }

    @Test
    public void testGetPersonByWrongId() throws Exception {
        UUID wrongId = UUID.randomUUID();

        RuntimeException exception = new EntityNotFoundException(Person.class, wrongId);
        when(personService.getPerson(wrongId)).thenThrow(exception);

        String resultJson = mvc.perform(get("/api/v1/persons/{id}", wrongId))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        assertTrue(resultJson.contains(exception.getMessage()));
    }

    @Test
    public void testGetPersonWithNotValidId() throws Exception {
        mvc.perform(get("/api/v1/persons/{id}", 42)).andExpect(status().isBadRequest());

        verifyNoInteractions(personService);
    }

    @Test
    public void testAddPerson() throws Exception {
        PersonCreateDto personCreateDto = utils.createPersonCreateDto();
        PersonReadDto personReadDto = utils.createPersonReadDto();

        when(personService.addPerson(personCreateDto)).thenReturn(personReadDto);

        String resultJson = mvc.perform(post("/api/v1/persons")
                .content(objectMapper.writeValueAsString(personCreateDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        PersonReadDto actualPersonReadDto = objectMapper.readValue(resultJson, PersonReadDto.class);
        assertThat(actualPersonReadDto).isEqualToComparingFieldByField(personReadDto);
    }

    @Test
    public void testPatchPerson() throws Exception {
        UUID id = UUID.randomUUID();
        PersonPatchDto personPatchDto = utils.createPersonPatchDto();

        PersonReadDto personReadDto = utils.createPersonReadDto();

        when(personService.patchPerson(id, personPatchDto)).thenReturn(personReadDto);

        String resultJson = mvc.perform(patch("/api/v1/persons/" + id)
                .content(objectMapper.writeValueAsString(personPatchDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        PersonReadDto actualPersonReadDto = objectMapper.readValue(resultJson, PersonReadDto.class);
        assertThat(actualPersonReadDto).isEqualToComparingFieldByField(personReadDto);
    }

    @Test
    public void testDeletePerson() throws Exception {
        UUID id = UUID.randomUUID();

        mvc.perform(delete("/api/v1/persons/" + id)).andExpect(status().isOk());

        verify(personService).deletePerson(id);
    }
}