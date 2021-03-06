package com.solvve.course.controller;

import com.solvve.course.BaseControllerTest;
import com.solvve.course.domain.Character;
import com.solvve.course.dto.character.CharacterCreateDto;
import com.solvve.course.dto.character.CharacterPatchDto;
import com.solvve.course.dto.character.CharacterReadDto;
import com.solvve.course.exception.EntityNotFoundException;
import com.solvve.course.service.CharacterService;
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

@WebMvcTest(controllers = CharacterController.class)
public class CharacterControllerTest extends BaseControllerTest {

    @MockBean
    private CharacterService characterService;

    @Test
    public void testGetCharacter() throws Exception {
        CharacterReadDto characterReadDto = utils.createCharacterReadDto();
        when(characterService.getCharacter(characterReadDto.getId())).thenReturn(characterReadDto);

        String resultJson = mvc.perform(get("/api/v1/characters/{id}", characterReadDto.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        CharacterReadDto actualMovie = objectMapper.readValue(resultJson, CharacterReadDto.class);

        assertEquals(actualMovie, characterReadDto);
    }

    @Test
    public void testGetCharacterByWrongId() throws Exception {
        UUID wrongId = UUID.randomUUID();

        RuntimeException exception = new EntityNotFoundException(Character.class, wrongId);
        when(characterService.getCharacter(wrongId)).thenThrow(exception);

        String resultJson = mvc.perform(get("/api/v1/characters/{id}", wrongId))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        assertTrue(resultJson.contains(exception.getMessage()));
    }

    @Test
    public void testGeCharacterWithNotValidId() throws Exception {
        mvc.perform(get("/api/v1/characters/{id}", 42)).andExpect(status().isBadRequest());

        verifyNoInteractions(characterService);
    }

    @Test
    public void testAddCharacter() throws Exception {
        CharacterCreateDto characterCreateDto = new CharacterCreateDto();
        characterCreateDto.setName("Sparrow");
        characterCreateDto.setActorId(UUID.randomUUID());
        characterCreateDto.setMovieId(UUID.randomUUID());
        CharacterReadDto characterReadDto = utils.createCharacterReadDto();

        when(characterService.addCharacter(characterCreateDto)).thenReturn(characterReadDto);

        String resultJson = mvc.perform(post("/api/v1/characters")
                .content(objectMapper.writeValueAsString(characterCreateDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        CharacterReadDto actualCharacterReadDto = objectMapper.readValue(resultJson, CharacterReadDto.class);
        assertThat(actualCharacterReadDto).isEqualToComparingFieldByField(characterReadDto);
    }

    @Test
    public void testPatchCharacter() throws Exception {
        UUID id = UUID.randomUUID();
        CharacterPatchDto characterPatchDto = utils.createCharacterPatchDto();

        CharacterReadDto characterReadDto = utils.createCharacterReadDto();

        when(characterService.patchCharacter(id, characterPatchDto)).thenReturn(characterReadDto);

        String resultJson = mvc.perform(patch("/api/v1/characters/" + id)
                .content(objectMapper.writeValueAsString(characterPatchDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        CharacterReadDto actualCharacterReadDto = objectMapper.readValue(resultJson, CharacterReadDto.class);
        assertThat(actualCharacterReadDto).isEqualToComparingFieldByField(characterReadDto);
    }

    @Test
    public void testDeleteCharacter() throws Exception {
        UUID id = UUID.randomUUID();

        mvc.perform(delete("/api/v1/characters/" + id)).andExpect(status().isOk());

        verify(characterService).deleteCharacter(id);
    }
}