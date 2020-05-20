package com.solvve.course.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.solvve.course.BaseControllerTest;
import com.solvve.course.domain.User;
import com.solvve.course.dto.correction.CorrectionReadDto;
import com.solvve.course.dto.user.UserCreateDto;
import com.solvve.course.dto.user.UserPatchDto;
import com.solvve.course.dto.user.UserReadDto;
import com.solvve.course.exception.EntityNotFoundException;
import com.solvve.course.service.UserService;
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

@WebMvcTest(controllers = UserController.class)
public class UserControllerTest extends BaseControllerTest {

    @MockBean
    private UserService userService;

    @Test
    public void testGetUser() throws Exception {
        UserReadDto userReadDto = utils.createUserReadDto();
        when(userService.getUser(userReadDto.getId())).thenReturn(userReadDto);

        String resultJson = mvc.perform(get("/api/v1/users/{id}", userReadDto.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        UserReadDto actualPrincipal = objectMapper.readValue(resultJson, UserReadDto.class);

        assertEquals(actualPrincipal, userReadDto);
    }

    @Test
    public void testGetUserCorrections() throws Exception {
        UserReadDto userReadDto = utils.createUserReadDto();
        CorrectionReadDto correctionReadDto = utils.createCorrectionReadDto();
        when(userService.getUserCorrections(userReadDto.getId()))
                .thenReturn(Collections.singletonList(correctionReadDto));

        String resultJson = mvc.perform(get("/api/v1/users/{id}/corrections", userReadDto.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        List<CorrectionReadDto> actualCorrections = objectMapper.readValue(resultJson, new TypeReference<>() {
        });

        assertEquals(Collections.singletonList(correctionReadDto), actualCorrections);
    }

    @Test
    public void testGetUserByWrongId() throws Exception {
        UUID wrongId = UUID.randomUUID();

        RuntimeException exception = new EntityNotFoundException(User.class, wrongId);
        when(userService.getUser(wrongId)).thenThrow(exception);

        String resultJson = mvc.perform(get("/api/v1/users/{id}", wrongId))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        assertTrue(resultJson.contains(exception.getMessage()));
    }

    @Test
    public void testGetUserWithNotValidId() throws Exception {
        mvc.perform(get("/api/v1/users/{id}", 42)).andExpect(status().isBadRequest());

        verifyNoInteractions(userService);
    }

    @Test
    public void testAddUser() throws Exception {
        UserCreateDto userCreateDto = new UserCreateDto();
        userCreateDto.setPrincipalId(UUID.randomUUID());
        UserReadDto userReadDto = utils.createUserReadDto();

        when(userService.addUser(userCreateDto)).thenReturn(userReadDto);

        String resultJson = mvc.perform(post("/api/v1/users")
                .content(objectMapper.writeValueAsString(userCreateDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        UserReadDto actualUserReadDto = objectMapper.readValue(resultJson, UserReadDto.class);
        assertThat(actualUserReadDto).isEqualToComparingFieldByField(userReadDto);
    }

    @Test
    public void testPatchUser() throws Exception {
        UUID id = UUID.randomUUID();
        UserPatchDto userPatchDto = utils.createUserPatchDto();

        UserReadDto userReadDto = utils.createUserReadDto();

        when(userService.patchUser(id, userPatchDto)).thenReturn(userReadDto);

        String resultJson = mvc.perform(patch("/api/v1/users/{id}", id)
                .content(objectMapper.writeValueAsString(userPatchDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        UserReadDto actualUserReadDto = objectMapper.readValue(resultJson, UserReadDto.class);
        assertThat(actualUserReadDto).isEqualToComparingFieldByField(userReadDto);
    }

    @Test
    public void testDeleteUser() throws Exception {
        UUID id = UUID.randomUUID();

        mvc.perform(delete("/api/v1/users/{id}", id)).andExpect(status().isOk());

        verify(userService).deleteUser(id);
    }
}