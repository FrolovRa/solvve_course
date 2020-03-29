package com.solvve.course.controller;

import com.solvve.course.BaseControllerTest;
import com.solvve.course.domain.Actor;
import com.solvve.course.dto.actor.ActorCreateDto;
import com.solvve.course.dto.actor.ActorExtendedReadDto;
import com.solvve.course.dto.actor.ActorPatchDto;
import com.solvve.course.dto.actor.ActorPutDto;
import com.solvve.course.exception.EntityNotFoundException;
import com.solvve.course.service.ActorService;
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

@WebMvcTest(controllers = ActorController.class)
public class ActorControllerTest extends BaseControllerTest {

    @MockBean
    private ActorService actorService;

    @Test
    public void testGetActor() throws Exception {
        ActorExtendedReadDto actorReadDto = utils.createActorExtendedReadDto();
        when(actorService.getActor(actorReadDto.getId())).thenReturn(actorReadDto);

        String resultJson = mvc.perform(get("/api/v1/actors/{id}", actorReadDto.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        ActorExtendedReadDto actualMovie = objectMapper.readValue(resultJson, ActorExtendedReadDto.class);

        assertEquals(actualMovie, actorReadDto);
    }

    @Test
    public void testGetActorByWrongId() throws Exception {
        UUID wrongId = UUID.randomUUID();

        RuntimeException exception = new EntityNotFoundException(Actor.class, wrongId);
        when(actorService.getActor(wrongId)).thenThrow(exception);

        String resultJson = mvc.perform(get("/api/v1/actors/{id}", wrongId))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        assertTrue(resultJson.contains(exception.getMessage()));
    }

    @Test
    public void testGeActorWithNotValidId() throws Exception {
        mvc.perform(get("/api/v1/actors/{id}", 42)).andExpect(status().isBadRequest());

        verifyNoInteractions(actorService);
    }

    @Test
    public void testAddActor() throws Exception {
        ActorCreateDto actorCreateDto = utils.createActorCreateDto();
        ActorExtendedReadDto actorReadDto = utils.createActorExtendedReadDto();

        when(actorService.addActor(actorCreateDto)).thenReturn(actorReadDto);

        String resultJson = mvc.perform(post("/api/v1/actors")
                .content(objectMapper.writeValueAsString(actorCreateDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        ActorExtendedReadDto actualActorReadDto = objectMapper.readValue(resultJson, ActorExtendedReadDto.class);
        assertThat(actualActorReadDto).isEqualToComparingFieldByField(actorReadDto);
    }

    @Test
    public void testPutActor() throws Exception {
        UUID id = UUID.randomUUID();
        ActorPutDto actorPutDto = utils.createActorPutDto();
        ActorExtendedReadDto actorReadDto = utils.createActorExtendedReadDto();

        when(actorService.updateActor(id, actorPutDto)).thenReturn(actorReadDto);

        String resultJson = mvc.perform(put("/api/v1/actors/" + id)
                .content(objectMapper.writeValueAsString(actorPutDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        ActorExtendedReadDto actualActorReadDto = objectMapper.readValue(resultJson, ActorExtendedReadDto.class);
        assertThat(actualActorReadDto).isEqualToComparingFieldByField(actorReadDto);
    }

    @Test
    public void testPatchActor() throws Exception {
        UUID id = UUID.randomUUID();
        ActorPatchDto actorPatchDto = utils.createActorPatchDto();

        ActorExtendedReadDto actorReadDto = utils.createActorExtendedReadDto();

        when(actorService.patchActor(id, actorPatchDto)).thenReturn(actorReadDto);

        String resultJson = mvc.perform(patch("/api/v1/actors/" + id)
                .content(objectMapper.writeValueAsString(actorPatchDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        ActorExtendedReadDto actualActorReadDto = objectMapper.readValue(resultJson, ActorExtendedReadDto.class);
        assertThat(actualActorReadDto).isEqualToComparingFieldByField(actorReadDto);
    }

    @Test
    public void testDeleteActor() throws Exception {
        UUID id = UUID.randomUUID();

        mvc.perform(delete("/api/v1/actors/" + id)).andExpect(status().isOk());

        verify(actorService).deleteActor(id);
    }
}