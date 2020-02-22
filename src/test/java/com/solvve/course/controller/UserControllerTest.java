package com.solvve.course.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.solvve.course.domain.User;
import com.solvve.course.dto.user.UserCreateDto;
import com.solvve.course.dto.user.UserPatchDto;
import com.solvve.course.dto.user.UserReadDto;
import com.solvve.course.exception.EntityNotFoundException;
import com.solvve.course.service.UserService;
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
@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private TestUtils utils = new TestUtils();

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