package com.solvve.course.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.solvve.course.domain.Movie;
import com.solvve.course.domain.constant.Genre;
import com.solvve.course.dto.MovieCreateDto;
import com.solvve.course.dto.MovieReadDto;
import com.solvve.course.exception.EntityNotFoundException;
import com.solvve.course.service.MovieService;
import org.junit.Assert;
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
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = MovieController.class)
public class MovieControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MovieService movieService;

    @Test
    public void testGetMovie() throws Exception {
        MovieReadDto movieReadDto = new MovieReadDto();
        movieReadDto.setId(UUID.randomUUID());
        movieReadDto.setName("Mr.Nobody");
        movieReadDto.setDescription("cool film");
        movieReadDto.setGenres(Collections.singleton(Genre.ACTION));
        when(movieService.getMovie(movieReadDto.getId())).thenReturn(movieReadDto);

        String resultJson = mvc.perform(get("/api/v1/movies/{id}", movieReadDto.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        MovieReadDto actualMovie = objectMapper.readValue(resultJson, MovieReadDto.class);

        assertEquals(actualMovie, movieReadDto);
    }

    @Test
    public void testGetMovieByWrongId() throws Exception {
        UUID wrongId = UUID.randomUUID();

        RuntimeException exception = new EntityNotFoundException(Movie.class, wrongId);
        when(movieService.getMovie(wrongId)).thenThrow(exception);

        String resultJson = mvc.perform(get("/api/v1/movies/{id}", wrongId))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains(exception.getMessage()));
    }

    @Test
    public void testGetMovieWithNotValidId() throws Exception {
        mvc.perform(get("/api/v1/movies/{id}", 42)).andExpect(status().isBadRequest());

        verifyNoInteractions(movieService);
    }

    @Test
    public void testAddMovie() throws Exception {
        MovieCreateDto movieCreateDto = new MovieCreateDto();
        movieCreateDto.setName("Mr.Nobody");
        movieCreateDto.setDescription("cool film");
        movieCreateDto.setGenres(Collections.singleton(Genre.ACTION));

        MovieReadDto movieReadDto = new MovieReadDto();
        movieReadDto.setId(UUID.randomUUID());
        movieReadDto.setName("Mr.Nobody");
        movieReadDto.setDescription("cool film");
        movieReadDto.setGenres(Collections.singleton(Genre.ACTION));

        when(movieService.addMovie(movieCreateDto)).thenReturn(movieReadDto);

        String resultJson = mvc.perform(post("/api/v1/movies")
                .content(objectMapper.writeValueAsString(movieCreateDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        MovieReadDto actualMovieReadDto = objectMapper.readValue(resultJson, MovieReadDto.class);
        assertThat(actualMovieReadDto).isEqualToComparingFieldByField(movieReadDto);
    }
}